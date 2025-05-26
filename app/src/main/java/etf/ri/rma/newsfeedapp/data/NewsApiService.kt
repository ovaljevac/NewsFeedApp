package etf.ri.rma.newsfeedapp.data

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/news/top")
    suspend fun getTopNewsByCategory(
        @Query("category") category: String,
        @Query("api_token") apiToken: String,
        @Query("limit") limit: Int = 3,
        @Query("locale") locale: String = "en"
    ): NewsApiResponse
}