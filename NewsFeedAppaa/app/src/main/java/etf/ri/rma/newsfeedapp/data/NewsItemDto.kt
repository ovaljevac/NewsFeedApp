package etf.ri.rma.newsfeedapp.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NewsItemDto(
    val uuid: String,
    val title: String,
    val description: String,
    val snippet: String,
    val url: String,
    val image_url: String,
    val language: String,
    val published_at: String,
    val source: String,
    val categories: List<String>,
    val relevance_score: Double?,
    val locale: String
)

fun NewsItemDto.toNewsItem(): NewsItem {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val formattedDate = try {
        val parsedDate = inputFormat.parse(published_at)
        outputFormat.format(parsedDate ?: Date())
    } catch (e: Exception) {
        "1970-01-01"
    }

    return NewsItem(
        uuid = uuid,
        title = title,
        description = description,
        snippet = snippet,
        url = url,
        imageUrl = image_url,
        language = language,
        publishedDate = formattedDate,
        source = source,
        category = categories.firstOrNull { it != "general" } ?: categories.firstOrNull() ?: "general",
        relevanceScore = relevance_score,
        locale = locale ?: "us",
        isFeatured = false
    )
}
