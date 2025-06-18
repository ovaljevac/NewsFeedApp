package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import etf.ri.rma.newsfeedapp.data.NewsItem


@Composable
fun FeaturedNewsCard(
    news: NewsItem,
    onClick: () -> Unit = {}
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .clickable(onClick = onClick)
    ){
        Column {
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl),
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = news.title,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = news.snippet
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = news.source + " • " + news.publishedDate
            )
        }
        }
}