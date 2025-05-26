package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

data class NewsItemDto(
    val uuid: String,
    val title: String,
    val description: String?,
    val source: String?,
    val published_at: String?,
    val url: String?,
    val image_url: String?,
    val category: String?
)

fun NewsItemDto.toNewsItem(): NewsItem {
    return NewsItem(
        uuid = uuid,
        title = title,
        snippet = description ?: "",
        imageUrl = image_url,
        category = category ?: "general",
        isFeatured = false,
        source = source ?: "",
        publishedDate = published_at ?: ""
    )
}