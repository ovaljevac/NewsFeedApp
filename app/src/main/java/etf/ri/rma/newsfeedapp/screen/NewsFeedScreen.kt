package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.model.FilterViewModel
import etf.ri.rma.newsfeedapp.model.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsFeedScreen(
    navController: NavController,
    viewModel: FilterViewModel,
    newsViewModel: NewsViewModel = viewModel()
) {
    val newsItemsAll = newsViewModel.newsItems
    val selectedCategory = viewModel.selectedCategory
    val selectedDateRange = viewModel.selectedDateRange
    val selectedUnwantedWords = viewModel.unwantedWords
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val apiCategory = viewModel.categoryMap[selectedCategory]

    val newsItemsFilter = newsItemsAll.filter { newsItem ->
        val categoryMatch = selectedCategory == "All" || newsItem.category == apiCategory
        val dateMatch = selectedDateRange?.let { (start, end) ->
            val itemDate = runCatching { formatter.parse(newsItem.publishedDate)?.time }.getOrNull()
            itemDate != null && itemDate in (start ?: Long.MIN_VALUE)..(end ?: Long.MAX_VALUE)
        } != false
        val unwantedMatch = selectedUnwantedWords.none { unwantedWord ->
            newsItem.title.contains(unwantedWord, ignoreCase = true) ||
                newsItem.snippet.contains(unwantedWord, ignoreCase = true)
        }
        categoryMatch && dateMatch && unwantedMatch
    }

    LaunchedEffect(selectedCategory) {
        when (selectedCategory) {
            "All" -> newsViewModel.loadAllStories()
            "More filters ..." -> Unit
            else -> apiCategory?.let { newsViewModel.loadTopStoriesByCategory(it) }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NewsBottomBar(
                active = "home",
                onHome = {},
                onExplore = { navController.navigate("filter") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 18.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("News")
                            }
                            append(" Flash")
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Good morning",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                SearchCircleButton()
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(viewModel.categories) { category ->
                    FilterChipCustom(
                        category = category,
                        selected = selectedCategory,
                        onSelected = {
                            if (it != "More filters ...") {
                                viewModel.selectedCategory = it
                            }
                        },
                        filterScreen = { navController.navigate("filter") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (newsItemsFilter.isEmpty() && selectedCategory != "More filters ...") {
                MessageCard("No news found in the $selectedCategory category")
            } else {
                key(selectedCategory) {
                    NewsList(
                        newsList = newsItemsFilter,
                        onItemClick = { news -> navController.navigate("details/${news.uuid}") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
