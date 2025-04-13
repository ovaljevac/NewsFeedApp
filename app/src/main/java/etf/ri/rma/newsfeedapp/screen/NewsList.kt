package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun NewsList(news_list: List<NewsItem>){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(news_list){ news ->
            FeaturedNewsCard(news = news)

        }
    }
}