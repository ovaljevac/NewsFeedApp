package etf.ri.rma.newsfeedapp.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import etf.ri.rma.newsfeedapp.data.NewsApiService
import etf.ri.rma.newsfeedapp.data.NewsDAO
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsViewModel : ViewModel() {
    private val apiToken = "hAGLP3RtqEQQySr2BTx1Z5gpQ8DhsBhde5IesLab"
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thenewsapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(NewsApiService::class.java)
    private val dao = NewsDAO(api, apiToken)
    val newsItems = mutableStateListOf<NewsItem>()

    init {
        val initial = dao.getAllStories()
        newsItems.clear()
        newsItems.addAll(initial)
    }




    fun loadTopStories(category: String) {
        viewModelScope.launch {
            try {
                val result = dao.getTopStoriesByCategory(category)
                newsItems.clear()
                newsItems.addAll(result)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Greška pri učitavanju vijesti: ${e.message}")
            }
        }
    }

    fun loadAllStories(): List<NewsItem> {
        return dao.getAllStories()
    }

    fun loadSimilarStories(id: String, onResult: (List<NewsItem>) -> Unit) {
        viewModelScope.launch {
            val result = dao.getSimilarStories(id)
            onResult(result)
        }
    }
}