package etf.ri.rma.newsfeedapp.api

import etf.ri.rma.newsfeedapp.data.ImagaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagaApiService {
    @GET("v2/tags")
    suspend fun getTags(
        @Query("image_url") imageUrl: String
    ) : ImagaResponse
}