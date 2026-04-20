package etf.ri.rma.newsfeedapp.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class NewsItemDto(
    val uuid: String,
    val title: String,
    val description: String,
    val snippet: String,
    val url: String,
    val image_url: String?,
    val language: String,
    val published_at: String,
    val source: String,
    val categories: List<String>,
    val relevance_score: Double?,
    val locale: String
)

fun NewsItemDto.toNewsItem(): NewsItem {
    val inputFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
    )
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val utc = TimeZone.getTimeZone("UTC")

    val formattedDate = inputFormats.firstNotNullOfOrNull { format ->
        try {
            format.timeZone = utc
            format.parse(published_at)?.let { outputFormat.format(it) }
        } catch (_: Exception) {
            null
        }
    } ?: outputFormat.format(Date())

    return NewsItem(
        uuid = uuid,
        title = title,
        description = description,
        snippet = snippet,
        url = url,
        imageUrl = image_url.orEmpty(),
        language = language,
        publishedDate = formattedDate,
        source = source,
        category = categories.firstOrNull { it != "general" } ?: categories.firstOrNull() ?: "general",
        relevanceScore = relevance_score,
        locale = locale,
        isFeatured = false
    )
}
