package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.model.NewsViewModel

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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(news.imageUrl),
            contentDescription = news.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = news.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.testTag("details_title")
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(
                onClick = {},
                label = { Text(translatedCategory) },
                modifier = Modifier.testTag("details_category")
            )
            Text(
                text = "${news.source} • ${news.publishedDate}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag("details_source")
            )
            Text(
                text = news.publishedDate,
                modifier = Modifier.testTag("details_date")
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = news.snippet,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("details_snippet")
        )

        if (news.imageTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Tagovi slike",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = news.imageTags.joinToString(", "),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .testTag("details_image_tags"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Povezane vijesti",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            viewModel.similarNewsItems.forEachIndexed { index, related ->
                RelatedNewsCard(
                    news = related,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("related_news_title_${index + 1}"),
                    onClick = { onNewsSelected(related) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .testTag("details_close_button"),
            shape = RoundedCornerShape(8.dp),
            onClick = onBack
        ) {
            Text("Nazad")
        }
    }
}

@Composable
private fun RelatedNewsCard(
    news: NewsItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl),
                contentDescription = news.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = news.title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
