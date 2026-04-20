package etf.ri.rma.newsfeedapp.api

import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.data.toNewsItem
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

    suspend fun refreshTopStories(
        locale: String = "us",
        limit: Int = 12
    ): List<NewsItem> {
        val currentTime = System.currentTimeMillis()
        val lastCalled = lastGetTime["__all__"] ?: 0L
        if (currentTime - lastCalled < 30_000L && allStories.isNotEmpty()) {
            return getAllStories()
        }

        return try {
            val response = api.getTopNews(apiToken, locale, "en", limit)
            val freshItems = response.data.mapIndexed { index, dto ->
                dto.toNewsItem().copy(isFeatured = index == 0)
            }
            mergeStories(freshItems)
            lastGetTime["__all__"] = currentTime
            getAllStories()
        } catch (e: Exception) {
            e.printStackTrace()
            getAllStories()
        }
    }

    suspend fun getTopStoriesByCategory(
        category: String,
        locale: String = "us",
        limit: Int = 5
    ): List<NewsItem> {
        val currentTime = System.currentTimeMillis()
        val lastCalled = lastGetTime[category] ?: 0L
        val existingNews = newsByCategory[category]?.toMutableList() ?: mutableListOf()

        if (currentTime - lastCalled < 30_000L && existingNews.isNotEmpty()) {
            return existingNews
        }

        return try {
            val response = api.getTopNewsByCategory(apiToken, category, locale, "en", limit)
            val newItemsRaw = response.data.mapIndexed { index, dto ->
                dto.toNewsItem().copy(isFeatured = index == 0)
            }
            val standard = existingNews
                .filter { oldItem -> newItemsRaw.none { it.uuid == oldItem.uuid } }
                .map { it.copy(isFeatured = false) }
            val finalList = newItemsRaw + standard
            newsByCategory[category] = finalList
            mergeStories(finalList)
            lastGetTime[category] = currentTime
            finalList
        } catch (e: Exception) {
            e.printStackTrace()
            existingNews.ifEmpty { getAllStories().filter { it.category == category } }
        }
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        if (!isValidUUID(uuid)) {
            throw InvalidUUIDException("Nepostojeci UUID: $uuid")
        }

        return try {
            val response = api.getSimilarNewsByUUID(uuid, apiToken)
            val result = response.data.map { it.toNewsItem() }
            mergeStories(result)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            throw InvalidUUIDException("Greska prilikom dohvata slicnih vijesti za UUID: $uuid")
        }
    }

    fun getAllStories(): List<NewsItem> {
        if (isFirstLoad) {
            mergeStories(NewsData.initialNews)
            isFirstLoad = false
        }
        return allStories
            .distinctBy { it.uuid }
            .sortedWith(
                compareByDescending<NewsItem> { it.isFeatured }
                    .thenByDescending { it.publishedDate }
            )
    }

    private fun mergeStories(items: List<NewsItem>) {
        items.forEach { item ->
            val existingIndex = allStories.indexOfFirst { it.uuid == item.uuid }
            if (existingIndex >= 0) {
                allStories[existingIndex] = if (item.isFeatured) item else allStories[existingIndex]
            } else {
                allStories.add(item)
            }

            val categoryList = newsByCategory[item.category]?.toMutableList() ?: mutableListOf()
            val categoryIndex = categoryList.indexOfFirst { it.uuid == item.uuid }
            if (categoryIndex >= 0) {
                categoryList[categoryIndex] = if (item.isFeatured) item else categoryList[categoryIndex]
            } else {
                categoryList.add(item)
            }
            newsByCategory[item.category] = categoryList
        }
    }
}
