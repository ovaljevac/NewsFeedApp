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
            // 🟢 SPORTS
            NewsItem(
                uuid = "b1c32c14-085b-41b8-b445-e2248877b169",
                title = "Will Zalatoris undergoes surgery for herniated disks",
                snippet = "Open Extended Reactions\n\nFormer PGA Tour Rookie of the Year Will Zalatoris had another surgery to correct two herniated disks in his back and will miss the rest...",
                imageUrl = "https://a.espncdn.com/combiner/i?img=%2Fphoto%2F2024%2F0306%2Fr1300621_1296x729_16%2D9.jpg",
                category = "sports",
                isFeatured = false,
                source = "espn.com",
                publishedDate = "2025-05-26"
            ),
            NewsItem(
                uuid = "7bd78d1c-eb7c-429a-8652-3284cea874c7",
                title = "Sportsnet.ca",
                snippet = "",
                imageUrl = "https://d2ml1l8vdyfdxz.cloudfront.net/favicon.ico",
                category = "sports",
                isFeatured = false,
                source = "sportsnet.ca",
                publishedDate = "2025-05-26"
            ),

            // 🟢 TECH
            NewsItem(
                uuid = "57c8fd90-04f9-4cce-9d61-1d921538286f",
                title = "Managerka wróciła do Ringier Axel Springer Polska po latach pracy w TVN",
                snippet = "Dla Katarzyny Szczekali to powrót to Ringier Axel Springer Polska. Kilka lat (2010-2013) pracowała tam u showbiznesu.\n\nPrzez ostatnie 11 lat radila je u TVN.",
                imageUrl = "https://static.wirtualnemedia.pl/media/new/googleDiscovery/6831f8fbba31d_katarzyna-szczekala.webp",
                category = "tech",
                isFeatured = false,
                source = "wirtualnemedia.pl",
                publishedDate = "2025-05-26"
            ),
            NewsItem(
                uuid = "f129e959-aeb6-4790-b0f1-924dbdfaddf0",
                title = "Дачникам раскрыли способы избавиться от колорадского жука",
                snippet = "Агроном Воронова: Народные способы борьбы с колорадским жуком бесполезны.",
                imageUrl = "https://icdn.lenta.ru/images/2025/05/26/23/20250526233817514/share_d6ce24a6b3b2fc877c367c646066b230.jpg",
                category = "tech",
                isFeatured = false,
                source = "lenta.ru",
                publishedDate = "2025-05-26"
            ),

            // 🟢 GENERAL
            NewsItem(
                uuid = "3422affa-984e-4a00-8475-0e29248771b3",
                title = "Farmers coming to terms with how much they've lost after devastating NSW floods",
                snippet = "Tony Buttsworth describes his farm on NSW's Mid North Coast as \"the heart and soul\" of his family. Now, the proud farmer can barely recognize it.",
                imageUrl = "https://live-production.wcms.abc-cdn.net.au/95205466e7c72088b09849eee69c5cfa?impolicy=wcms_watermark_news&cropH=1125&cropW=2000&xPos=0&yPos=139&width=862&height=485&imformat=generic",
                category = "general",
                isFeatured = false,
                source = "abc.net.au",
                publishedDate = "2025-05-26"
            ),
            NewsItem(
                uuid = "01713745-fe68-460e-998e-5734a414e77e",
                title = "Roque Dalton revive con un poemario inédito",
                snippet = "Hasta ahora, algunos textos sueltos del libro «El amor me cae más mal que la primavera» del poeta salvadoreño Roque Dalton se habían podido disfrutar.",
                imageUrl = "https://ultimasnoticias.com.ve/wp-content/uploads/2025/05/photo_5012667640266731179_y-web.jpg",
                category = "general",
                isFeatured = false,
                source = "ultimasnoticias.com.ve",
                publishedDate = "2025-05-26"
            ),

            // 🟢 POLITICS (ručno dodano iz general kategorije s političkim kontekstom)
            NewsItem(
                uuid = "ba88e999-0e4c-4d47-9d18-96a715cd0c8f",
                title = "Prensa salvadoreña denuncia “escalada autoritaria” de Bukele",
                snippet = "La Asociación de Periodistas de El Salvador denunció el aumento de la “persecución” contra medios de comunicación y defensores de derechos humanos.",
                imageUrl = "https://ultimasnoticias.com.ve/wp-content/uploads/2025/05/descarga-1.jpg",
                category = "politics",
                isFeatured = false,
                source = "ultimasnoticias.com.ve",
                publishedDate = "2025-05-26"
            ),
            NewsItem(
                uuid = "d6914f1a-fe91-4560-971e-6826a6d9f539",
                title = "Incêndio obriga a suspender operações na maior refinaria do Equador",
                snippet = "\"Temos um depósito de fuelóleo que se incendiou. A situação está sob controlo\", izjavila ministrica energetike.",
                imageUrl = "https://media-manager.noticiasaominuto.com/1280/naom_6834c22d40aa5.webp?crop_params=dW5kZWZpbmVk",
                category = "politics",
                isFeatured = false,
                source = "noticiasaominuto.com",
                publishedDate = "2025-05-26"
            ),

            // 🟢 SCIENCE (ručno dodano — simulirana vijest)
            NewsItem(
                uuid = "sci-001",
                title = "NASA otkrila novu egzoplanetu u zoni pogodnoj za život",
                snippet = "Tim naučnika je potvrdio postojanje planete koja orbitira unutar nastanjive zone udaljene zvijezde.",
                imageUrl = null,
                category = "science",
                isFeatured = false,
                source = "NASA",
                publishedDate = "2025-05-25"
            ),
            NewsItem(
                uuid = "sci-002",
                title = "Znanstvenici razvili novi materijal lakši i jači od čelika",
                snippet = "Materijal na bazi grafena pokazuje izuzetnu čvrstoću i fleksibilnost, s mogućnostima primjene u građevini i medicini.",
                imageUrl = null,
                category = "science",
                isFeatured = false,
                source = "Science Daily",
                publishedDate = "2025-05-24"
            )
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