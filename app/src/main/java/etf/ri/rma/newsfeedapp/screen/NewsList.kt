package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsItem

@Composable
fun NewsList(
    newsList: List<NewsItem>,
    onItemClick: (NewsItem) -> Unit
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("news_list")
        ,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(newsList){ news ->
            if(news.isFeatured){
                FeaturedNewsCard(news = news, onClick = {onItemClick(news)})
            }
            else {
                StandardNewsCard(news = news, onClick = {onItemClick(news)})
            }

        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
