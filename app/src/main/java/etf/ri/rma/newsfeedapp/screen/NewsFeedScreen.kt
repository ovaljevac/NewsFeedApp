package etf.ri.rma.newsfeedapp.screen

import androidx.compose.runtime.Composable
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem

class NewsFeedScreen {
    private val newsItems = NewsData.getAllNews()

    @Composable
    fun Load() {
        NewsList(news_list = newsItems)
    }
}