package etf.ri.rma.newsfeedapp.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import etf.ri.rma.newsfeedapp.R
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.model.NewsViewModel

@Composable
fun NewsDetailsScreen(
    news: NewsItem,
    onBack: () -> Unit,
    onNewsSelected: (NewsItem) -> Unit,
    viewModel: NewsViewModel = viewModel()
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    LaunchedEffect(news.uuid) {
        viewModel.loadSimilarStories(news.uuid)
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 88.dp)
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(news.imageUrl.takeIf { it.isNotBlank() } ?: R.drawable.default_banner),
                    contentDescription = news.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundIconButton(Icons.Filled.ArrowBack, "Back", onBack)
                    Spacer(modifier = Modifier.weight(1f))
                    RoundIconButton(Icons.Filled.Favorite, "Save", {})
                    Spacer(modifier = Modifier.size(10.dp))
                    RoundIconButton(Icons.Filled.Share, "Share", {})
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = readableCategory(news.category).uppercase(),
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .testTag("details_category"),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag("details_title")
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.padding(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(modifier = Modifier.padding(start = 10.dp)) {
                        Text(
                            text = news.source,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("details_source")
                        )
                        Text(
                            text = "${news.publishedDate}  -  4 min read",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.testTag("details_date")
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 18.dp),
                    color = MaterialTheme.colorScheme.outline
                )

                if (news.url.isNotBlank()) {
                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("details_story_link"),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                            )
                        }
                    ) {
                        Text(
                            text = news.url,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                ArticleBody(news)

                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = "Related News",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                val relatedNews = viewModel.similarNewsItems
                    .ifEmpty {
                        viewModel.newsItems
                            .filter { it.uuid != news.uuid && it.category == news.category }
                    }
                    .ifEmpty {
                        viewModel.newsItems.filter { it.uuid != news.uuid }
                    }
                    .take(2)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    relatedNews.forEachIndexed { index, related ->
                        RelatedNewsCard(
                            news = related,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("related_news_title_${index + 1}"),
                            onClick = { onNewsSelected(related) }
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(20.dp)
                .height(56.dp)
                .testTag("details_close_button"),
            shape = CircleShape,
            onClick = onBack
        ) {
            Text("Back")
        }
    }
}

@Composable
private fun ArticleBody(news: NewsItem) {
    val paragraphs = articleParagraphs(news)

    Column(
        modifier = Modifier.testTag("details_snippet"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        paragraphs.forEach { paragraph ->
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Serif,
                    lineHeight = 26.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun articleParagraphs(news: NewsItem): List<String> {
    val description = news.description.trim()
    val snippet = news.snippet.trim()
    val parts = when {
        description.isBlank() && snippet.isBlank() -> emptyList()
        description.isBlank() -> listOf(snippet)
        snippet.isBlank() -> listOf(description)
        description.contains(snippet) -> listOf(description)
        snippet.contains(description) -> listOf(snippet)
        else -> listOf(description, snippet)
    }

    return parts
        .ifEmpty { listOf("Full article text is unavailable for this story.") }
        .flatMap { it.split("\n").map(String::trim).filter(String::isNotBlank) }
}

@Composable
private fun RoundIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(46.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
        shadowElevation = 5.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurface
            )
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(news.imageUrl.takeIf { it.isNotBlank() } ?: R.drawable.default_img),
                contentDescription = news.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .clip(RoundedCornerShape(10.dp))
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
