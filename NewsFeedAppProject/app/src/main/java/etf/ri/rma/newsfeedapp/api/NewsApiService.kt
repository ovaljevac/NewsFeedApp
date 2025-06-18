package etf.ri.rma.newsfeedapp.api

import etf.ri.rma.newsfeedapp.data.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/news/top")
    suspend fun getTopNewsByCategory(
        @Query("api_token") apiToken: String,
        @Query("categories") category: String,
        @Query("locale") locale: String = "us",
        @Query("limit") limit: Int = 3
    ) : NewsResponse

    @GET("v1/news/similar/{uuid}")
    suspend fun getSimilarNewsByUUID(
        @Path("uuid") uuid: String,
        @Query("api_token") token: String,
        @Query("limit") limit: Int = 2
    ): NewsResponse
}