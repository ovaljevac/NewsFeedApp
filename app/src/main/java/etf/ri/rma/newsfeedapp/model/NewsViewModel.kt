package etf.ri.rma.newsfeedapp.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import etf.ri.rma.newsfeedapp.api.NewsDAO
import etf.ri.rma.newsfeedapp.api.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    val dao = NewsDAO(RetrofitInstance.newsApi, "issrUr8BskkxUd2GlM2vynh6PgTpnjTEO5hzPtrq")
    val newsItems = mutableStateListOf<NewsItem>()
    val similarNewsItems = mutableStateListOf<NewsItem>()
    private val lastLoadTime = mutableMapOf<String, Long>()

    fun loadAllStories() : List<NewsItem> {
        newsItems.clear()
        val result = dao.getAllStories()
        newsItems.addAll(result)
        return newsItems
    }

    fun loadTopStoriesByCategory(category: String, locale: String = "us", limit: Int = 3) {
        val now = System.currentTimeMillis()
        val lastTime = lastLoadTime[category] ?: 0L
        val timeSinceLast = now - lastTime
        if(timeSinceLast < 30_000L){
            return
        }
        viewModelScope.launch {
            try{
                val result = dao.getTopStoriesByCategory(category, locale, limit)
                val merged = (result + newsItems).distinctBy { it.uuid }
                newsItems.clear()
                newsItems.addAll(merged)
                lastLoadTime[category] = now
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSimilarStories(uuid: String) {
        viewModelScope.launch {
            try {
                val result = dao.getSimilarStories(uuid)
                similarNewsItems.clear()
                similarNewsItems.addAll(result)
                val newUniqueItems = result.filter { newItem ->
                    newsItems.none { it.uuid == newItem.uuid }
                }
                newsItems.addAll(newUniqueItems)
            } catch (e: InvalidUUIDException) {
                println("Greška: ${e.message}")
                similarNewsItems.clear()
            }
        }
    }

}