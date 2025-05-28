package etf.ri.rma.newsfeedapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val newsApi: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}