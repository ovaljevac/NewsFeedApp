package etf.ri.rma.newsfeedapp.model

import SavedNewsDAO
import android.Manifest
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import etf.ri.rma.newsfeedapp.api.ImagaDAO
import etf.ri.rma.newsfeedapp.api.NewsDAO
import etf.ri.rma.newsfeedapp.api.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel (application: Application) : AndroidViewModel(application) {
    val dao = NewsDAO(RetrofitInstance.newsApi, "issrUr8BskkxUd2GlM2vynh6PgTpnjTEO5hzPtrq")
    val newsItems = mutableStateListOf<NewsItem>()
    val similarNewsItems = mutableStateListOf<NewsItem>()
    private val lastLoadTime = mutableMapOf<String, Long>()
    val tempNewsItems = mutableListOf<NewsItem>()
    private val localDao: SavedNewsDAO = NewsDatabase.getInstance(application).newsDao()

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isConnected(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    fun loadAllStories() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                localDao.allNews()
            }
            newsItems.clear()
            newsItems.addAll(result)
        }
    }


    fun loadTopStoriesPreview(category: String, locale: String = "us", limit: Int = 3) {
        val now = System.currentTimeMillis()
        val lastTime = lastLoadTime[category] ?: 0L
        val timeSinceLast = now - lastTime
        if (timeSinceLast < 30_000L) {
            return
        }
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    dao.getTopStoriesByCategory(category, locale, limit)
                }
                tempNewsItems.clear()
                tempNewsItems.addAll(result)
                lastLoadTime[category] = now
            } catch (e: Exception) {
                e.printStackTrace()
                tempNewsItems.clear()
            }
        }
    }

    fun applyFilters() {
        newsItems.clear()
        newsItems.addAll(tempNewsItems)
        tempNewsItems.clear()
    }


    fun loadTopStoriesByCategory(category: String, locale: String = "us", limit: Int = 3) {
        val now = System.currentTimeMillis()
        val lastTime = lastLoadTime[category] ?: 0L
        val timeSinceLast = now - lastTime

        if (timeSinceLast < 30_000L) return

        viewModelScope.launch {
            if (isConnected()) {
                try {
                    val result = dao.getTopStoriesByCategory(category, locale, limit)
                    for (item in result) {
                        withContext(Dispatchers.IO) { localDao.saveNews(item) }
                    }
                    for (item in result) {
                        if (item.imageTags.isNotEmpty()) {
                            withContext(Dispatchers.IO) {
                                localDao.addTags(item.imageTags, localDao.getIdByUuid(item.uuid) ?: return@withContext)
                            }
                        }
                    }
                    val merged = (result + newsItems).distinctBy { it.uuid }
                    newsItems.clear()
                    newsItems.addAll(merged)
                    lastLoadTime[category] = now
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                val result = withContext(Dispatchers.IO) {
                    localDao.getNewsWithCategory(category)
                }
                newsItems.clear()
                newsItems.addAll(result)
            }
        }
    }


    fun loadSimilarStories(uuid: String) {
        viewModelScope.launch {
            if (isConnected()) {
                try {
                    val result = dao.getSimilarStories(uuid)
                    similarNewsItems.clear()
                    similarNewsItems.addAll(result)

                    val newUniqueItems = result.filter { newItem ->
                        newsItems.none { it.uuid == newItem.uuid }
                    }
                    newsItems.addAll(newUniqueItems)
                } catch (e: InvalidUUIDException) {
                    similarNewsItems.clear()
                }
            } else {
                val news = withContext(Dispatchers.IO) {
                    localDao.allNews().find { it.uuid == uuid }
                } ?: return@launch

                val tags = withContext(Dispatchers.IO) {
                    localDao.getTags(newsId = localDao.getIdByUuid(uuid) ?: return@withContext emptyList())
                }

                val similar = withContext(Dispatchers.IO) {
                    localDao.getSimilarNews(tags).take(2)
                }

                similarNewsItems.clear()
                similarNewsItems.addAll(similar.filter { it.uuid != uuid })
            }
        }
    }

    fun loadImageTags(news: NewsItem) {
        viewModelScope.launch {
            if (news.imageTags.isNotEmpty()) return@launch

            val newsId = withContext(Dispatchers.IO) {
                localDao.getIdByUuid(news.uuid)
            }

            if (newsId != null) {
                val savedTags = withContext(Dispatchers.IO) {
                    localDao.getTags(newsId)
                }
                if (savedTags.isNotEmpty()) {
                    news.imageTags.addAll(savedTags)
                    return@launch
                }
            }

            if (isConnected()) {
                try {
                    val tags = ImagaDAO(RetrofitInstance.imagaApi).getTags(news.imageUrl)
                    news.imageTags.addAll(tags)

                    if (newsId != null) {
                        withContext(Dispatchers.IO) {
                            localDao.addTags(tags, newsId)
                        }
                    }
                } catch (_: Exception) { }
            }
        }
    }





}