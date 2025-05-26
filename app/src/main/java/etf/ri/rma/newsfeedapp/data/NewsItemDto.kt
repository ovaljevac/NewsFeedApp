package etf.ri.rma.newsfeedapp.data

import android.R.attr.category
import com.google.gson.annotations.SerializedName
import etf.ri.rma.newsfeedapp.model.NewsItem

data class NewsItemDto(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("snippet") val snippet: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("categories") val categories: List<String>?,
    @SerializedName("source") val source: String?,
    @SerializedName("published_at") val publishedAt: String?
){

fun toNewsItem(): NewsItem {
    val preferredCategory = categories?.firstOrNull { it != "general" } ?: categories?.firstOrNull() ?: "general"

    return NewsItem(
        uuid = uuid,
        title = title,
        snippet = snippet ?: (description ?: ""),
        imageUrl = imageUrl,
        category = preferredCategory,
        isFeatured = false,
        source = source ?: "",
        publishedDate = publishedAt?.substring(0, 10) ?: ""
    )
}
}