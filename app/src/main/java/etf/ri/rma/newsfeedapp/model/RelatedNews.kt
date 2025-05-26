package etf.ri.rma.newsfeedapp.model

import etf.ri.rma.newsfeedapp.data.NewsData
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

data class RelatedNews(
    val news: NewsItem,
    val dateDiff: Long,
    val title: String
)

fun getRelatedNews(currentNews: NewsItem): List<NewsItem> {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val currentDate = formatter.parse(currentNews.publishedDate)?.time

    return NewsData.getAllNews()
        .filter { it.uuid != currentNews.uuid && it.category == currentNews.category }
        .mapNotNull { item ->
            val itemDate = formatter.parse(item.publishedDate)?.time
            itemDate?.let {
                val dateDiff = abs(it - (currentDate ?: it))
                RelatedNews(item, dateDiff, item.title)
            }
        }
        .sortedWith(
            compareBy<RelatedNews> { it.dateDiff }
                .thenBy { it.title }
        )
        .take(2)
        .map { it.news }
}