package etf.ri.rma.newsfeedapp.api

import android.util.Patterns
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException

class ImagaDAO(private val api: ImagaApiService) {

    private val tagCache = mutableMapOf<String, List<String>>()

    suspend fun getTags(imageURL: String): List<String> {
        if (!Patterns.WEB_URL.matcher(imageURL).matches()) {
            throw InvalidImageURLException()
        }
        tagCache[imageURL]?.let { return it }
        return try {
            val response = api.getTags(imageURL)
            val tags = response.result?.tags?.mapNotNull { it.tag["en"] } ?: emptyList()
            tagCache[imageURL] = tags
            tags
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
