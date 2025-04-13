package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem(
                id = "0",
                title = "Konaković izabran za vođu Steleksa",
                snippet = "Ma nije",
                category = "Politika",
                isFeatured = true,
                imageUrl = null,
                source = "Vesela sveska",
                publishedDate = "31.02.2001.",
            )
        )
    }
}