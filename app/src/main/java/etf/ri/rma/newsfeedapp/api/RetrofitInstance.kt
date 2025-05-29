package etf.ri.rma.newsfeedapp.api

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitInstance {
    val newsApi: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
    val imagaApi: ImagaApiService by lazy {
        val credentials = Credentials.basic("acc_1cff041f9b57936", "64d7ca5c7b1343b6f560ac6b7f29bce0")
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", credentials)
                    .build()
                chain.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .baseUrl("https://api.imagga.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImagaApiService::class.java)
    }
}