package etf.ri.rma.newsfeedapp

import etf.ri.rma.newsfeedapp.api.ImagaApiService
import etf.ri.rma.newsfeedapp.api.ImagaDAO
import etf.ri.rma.newsfeedapp.api.NewsApiService
import etf.ri.rma.newsfeedapp.api.NewsDAO
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class TestS3PripremljenRetrofit {
    fun getNewsDAOwithBaseURL(baseURL:String,httpClient:OkHttpClient): NewsDAO {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
        val newsApiService = retrofit.create(NewsApiService::class.java)
        val newsDAO = NewsDAO(newsApiService, "issrUr8BskkxUd2GlM2vynh6PgTpnjTEO5hzPtrq")
        return newsDAO
    }
    fun getImaggaDAOwithBaseURL(baseURL:String,httpClient:OkHttpClient):ImagaDAO{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(httpClient)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
        val imagaApiService = retrofit.create(ImagaApiService::class.java)
        val imagaDAO = ImagaDAO(imagaApiService)
        return imagaDAO
    }
}