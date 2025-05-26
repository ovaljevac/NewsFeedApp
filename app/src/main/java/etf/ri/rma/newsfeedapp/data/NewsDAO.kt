package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

class NewsDAO (
    private val api: NewsApiService,
    private val apiToken: String
) {
    val allowedCategories = listOf(
        "general", "science", "sports", "business",
        "entertainment", "tech", "politics", "food", "travel"
    )
    private val allStories = mutableListOf<NewsItem>()
    private var initialStoriesReturned = false
    private val lastGetTime = mutableMapOf<String, Long>()
    private val newsByCategory = mutableMapOf<String, MutableList<NewsItem>>()

    private fun now(): Long = System.currentTimeMillis()

    private fun getInitialStories(): List<NewsItem> {
        return listOf(
            NewsItem("1", "NASA otkrila novu planetu", "Zanimljivo otkriće u galaksiji XYZ", null, "Nauka/tehnologija", false, "NASA", "2024-05-01"),
            NewsItem("2", "Politička kriza u zemlji", "Nova vlada formirana", null, "Politika", true, "BBC", "2024-05-02"),
            NewsItem("3", "Rezultati fudbalske utakmice", "Tim A pobijedio Tim B", null, "Sport", false, "ESPN", "2024-05-03"),
            NewsItem("4", "Zdravstveni savjeti za ljeto", "Kako se zaštititi od sunca", null, "Health", false, "HealthLine", "2024-05-04"),
            NewsItem("5", "Najnoviji film dominira kinima", "Fantastična akcija", null, "Entertainment", true, "IMDb", "2024-05-05"),
            NewsItem("6", "Biznis strategije 2024", "Kako povećati profit?", null, "Business", false, "Forbes", "2024-05-06"),
            NewsItem("7", "Nova tehnološka inovacija", "Smartphone koji lebdi?", null, "Tech", false, "TechRadar", "2024-05-07"),
            NewsItem("8", "Recept dana: Musaka", "Lako i ukusno", null, "Food", false, "Coolinarika", "2024-05-08"),
            NewsItem("9", "Turistička sezona počinje", "Top 5 destinacija", null, "Travel", false, "TripAdvisor", "2024-05-09"),
            NewsItem("10", "Generalne vijesti", "Dogodilo se danas...", null, "General", false, "Al Jazeera", "2024-05-10"),
        )
    }


    suspend fun getTopStoriesByCategory(category: String) : List<NewsItem> {
        require(category in allowedCategories){
            "Nepoznata kategorija: $category"
        }
        val currentTime = now()
        val lastGet = lastGetTime[category] ?: 0L
        val timePassed = currentTime - lastGet
        val existing = newsByCategory[category]?.toMutableList() ?: mutableListOf()
        return if (timePassed<30_000L) {
            existing
        }  else {
            val newsFromApi = api.getTopNewsByCategory(category, apiToken)
                .data.map { it.toNewsItem() }
            val featuredNews = mutableListOf<NewsItem>()
            for (item in newsFromApi) {
                val existingItem = existing.find { it.uuid == item.uuid }
                if (existingItem != null) {
                    val updated = existingItem.copy(isFeatured = true)
                    existing.remove(existingItem)
                    existing.add(updated)
                    featuredNews.add(updated)
                } else {
                    val newFeatured = item.copy(isFeatured = true)
                    allStories.add(newFeatured)
                    existing.add(newFeatured)
                    featuredNews.add(newFeatured)
                }
                if (featuredNews.size == 3) break
            }
            val updateOthers = existing.map {
                if (featuredNews.any { f -> f.uuid == it.uuid }) it
                else it.copy(isFeatured = false)
            }
            newsByCategory[category] = updateOthers.toMutableList()
            lastGetTime[category] = currentTime
            featuredNews + updateOthers.filterNot { it.isFeatured }
        }

    }

    fun getAllStories(): List<NewsItem> {
        if(!initialStoriesReturned) {
            allStories.addAll(getInitialStories())
            initialStoriesReturned = true
        }
        return allStories.toList()
    }

    suspend fun getSimilarStories(id: String): List<NewsItem> {
        val originalNewsItem = allStories.find { it.uuid == id }
            ?: return emptyList()
        return allStories
            .filter { it.category == originalNewsItem.category && it.uuid != id }
            .take(2)
    }

}