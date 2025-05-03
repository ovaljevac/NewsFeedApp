package etf.ri.rma.newsfeedapp.screen

import etf.ri.rma.newsfeedapp.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.NewsItem




@Composable
fun StandardNewsCard(
    news: NewsItem,
    onClick: () -> Unit = {}
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)

    ){
        Row(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.default_img),
                contentDescription = "image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column (
                modifier = Modifier
                    .clickable(onClick = onClick)
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