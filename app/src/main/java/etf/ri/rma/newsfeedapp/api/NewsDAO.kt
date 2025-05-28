package etf.ri.rma.newsfeedapp.api

import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.toNewsItem
import etf.ri.rma.newsfeedapp.exceptions.InvalidUUIDException

class NewsDAO(
    private val api: NewsApiService,
    private val apiToken: String
) {
    private var isFirstLoad = true
    private val allStories = mutableListOf<NewsItem>()
    private val newsByCategory = mutableMapOf<String, List<NewsItem>>()
    private val lastGetTime = mutableMapOf<String, Long>()

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
                val featured = mutableListOf<NewsItem>()
                val remaining = existingNews.toMutableList()
                for (item in newItemsRaw) {
                    val existing = remaining.find { it.uuid == item.uuid }
                    if (existing != null) {
                        remaining.remove(existing)
                        featured.add(existing.copy(isFeatured = true))
                    } else {
                        val newFeatured = item.copy(isFeatured = true)
                        allStories.add(newFeatured)
                        featured.add(newFeatured)
                    }
                }
                val standard = remaining.map { it.copy(isFeatured = false) }
                val finalList = featured + standard
                newsByCategory[category] = finalList.toMutableList()
                lastGetTime[category] = currentTime
                return finalList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        val allNews = getAllStories()
        val targetNews = allNews.find { it.uuid == uuid }
            ?: throw InvalidUUIDException("Nepostojeći UUID: $uuid")
        val targetCategory = targetNews.category
        val similar = allNews
            .filter { it.category == targetCategory && it.uuid != uuid }
            .take(2)
        return similar
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
