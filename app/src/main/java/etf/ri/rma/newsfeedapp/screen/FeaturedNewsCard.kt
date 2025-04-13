package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem


@Composable
fun FeaturedNewsCard(news: NewsItem){
    Card (
        modifier = Modifier
            .fillMaxWidth()
    ){
//                Image(
//                    painter = painter,
//                    contentDescription = null
//                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.title,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = news.snippet
                )
                Text(
                    text = news.source + " " + news.publishedDate
                )
        }
}