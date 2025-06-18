package etf.ri.rma.newsfeedapp.api

import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.toNewsItem
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import java.util.UUID

class NewsDAO(
    private val api: NewsApiService,
    private val apiToken: String
) {
    private var isFirstLoad = true
    private val allStories = mutableListOf<NewsItem>()
    private val newsByCategory = mutableMapOf<String, List<NewsItem>>()
    private val lastGetTime = mutableMapOf<String, Long>()

    fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getTopStoriesByCategory(
        category: String,
        locale: String = "us",
        limit: Int = 3
    ): List<NewsItem> {
        val currentTime = System.currentTimeMillis()
        val lastCalled = lastGetTime[category] ?: 0L
        val timeSinceLastCall = currentTime - lastCalled
        val existingNews = newsByCategory[category]?.toMutableList() ?: mutableListOf()
        return if (timeSinceLastCall < 30_000L) {
            existingNews
        } else {
            return try {
                val response = api.getTopNewsByCategory(apiToken, category, locale, limit)
                val newItemsRaw = response.data.map { it.toNewsItem() }
                val featured = newItemsRaw.map { newItem ->
                    val existing = existingNews.find { it.uuid == newItem.uuid }
                    if (existing != null) {
                        existing.copy(isFeatured = true)
                    } else {
                        val newFeatured = newItem.copy(isFeatured = true)
                        allStories.add(newFeatured)
                        newFeatured
                    }
                }
                val standard = existingNews
                    .filter { oldItem -> newItemsRaw.none { it.uuid == oldItem.uuid } }
                    .map { it.copy(isFeatured = false) }
                val finalList = featured + standard
                newsByCategory[category] = finalList.toMutableList()
                lastGetTime[category] = currentTime
                finalList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        if(!isValidUUID(uuid))
            throw InvalidUUIDException("Nepostojeći UUID: $uuid")
        return try {
            val response = api.getSimilarNewsByUUID(uuid, apiToken)
            val result = response.data.map { it.toNewsItem() }
            val newUniqueItems = result.filter { newItem ->
                allStories.none { it.uuid == newItem.uuid}
            }
            newUniqueItems.forEach { item ->
                val categoryList = newsByCategory[item.category]?.toMutableList() ?: mutableListOf()
                categoryList.add(item)
                newsByCategory[item.category] = categoryList
            }
            allStories.addAll(newUniqueItems)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            throw InvalidUUIDException("Greška prilikom dohvata sličnih vijesti za UUID: $uuid")
        }
    }



    fun getAllStories(): List<NewsItem> {
        if (isFirstLoad) {
            allStories.addAll(NewsData.initialNews)
            isFirstLoad = false
        }
        return allStories
            .distinctBy { it.uuid }
            .sortedWith(
                compareByDescending<NewsItem> { it.isFeatured }
                    .thenByDescending { it.publishedDate }
            )
    }

}
