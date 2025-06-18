package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import etf.ri.rma.newsfeedapp.api.ImagaDAO
import etf.ri.rma.newsfeedapp.api.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import etf.ri.rma.newsfeedapp.model.NewsViewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun NewsDetailsScreen(
    news: NewsItem,
    onBack: () -> Unit,
    onNewsSelected: (NewsItem) -> Unit,
    viewModel: NewsViewModel = viewModel()
) {
    BackHandler {
        onBack()
    }
    LaunchedEffect(news.uuid) {
        viewModel.loadImageTags(news)
        viewModel.loadSimilarStories(news.uuid)
    }

    val categoryMap = mapOf(
        "politics" to "Politika",
        "sports" to "Sport",
        "science" to "Nauka",
        "tech" to "Tehnologija",
        "business" to "Biznis",
        "health" to "Zdravlje",
        "entertainment" to "Zabava",
        "food" to "Hrana",
        "travel" to "Putovanje"
    )
    val translatedCategory = categoryMap[news.category] ?: news.category
    var backButton by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(scrollState)
    ){
        Image(
            painter = rememberAsyncImagePainter(news.imageUrl),
            contentDescription = "image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(5.dp)
        )
        Text(
            text = news.title,
            fontWeight = Bold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(5.dp)
                .testTag("details_title")
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp),
            thickness = 2.dp,
            color = Color.Black
        )
        Text(
            text = news.snippet,
            fontSize = 19.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(5.dp)
                .testTag("details_snippet")
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp),
            thickness = 2.dp,
            color = Color.Black
        )
        Row (
            modifier = Modifier
                .padding(5.dp)
        ){
            Text(
                text = news.source,
                modifier = Modifier.testTag("details_source")
            )
            Text(
                text = " • ",
            )
            Text(
                text = news.publishedDate,
                modifier = Modifier.testTag("details_date")
            )
        }
        if (news.imageTags.isNotEmpty()) {
            Text(
                text = "Tagovi slike: ${news.imageTags.joinToString(", ")}",
                modifier = Modifier
                    .padding(5.dp)
                    .testTag("details_image_tags"),
                fontSize = 14.sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "KATEGORIJA: $translatedCategory \nPOVEZANE VIJESTI IZ ISTE KATEGORIJE:",
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 5.dp)
                .testTag("details_category")
        )
            Row {
                viewModel.similarNewsItems.forEachIndexed { index, related ->
                Column (
                    modifier = Modifier
                        .weight(0.5f)
                        .clickable { onNewsSelected(related) }
                        .padding(5.dp)
                        .testTag("related_news_title_${index + 1}")
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = rememberAsyncImagePainter(related.imageUrl),
                        contentDescription = "image",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 12.dp)
                    )
                        Text(
                            text = related.title,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal =5.dp) ,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())

        ){
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A3A3A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 5.dp)
                    .height(53.dp)
                    .weight(0.5f)
                    .testTag("details_close_button"),
                onClick = {
                    backButton = !backButton
                    onBack()
                }
            ) {
                Text("Nazad")
            }
        }
    }
}