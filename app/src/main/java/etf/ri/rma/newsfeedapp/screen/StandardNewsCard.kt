package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.NewsItem

@Composable
fun StandardNewsCard(news: NewsItem){
    Card (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding (end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "IMG"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column (
            ){
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = news.title,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
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
}