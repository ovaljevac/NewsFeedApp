package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
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
    val newsItemsFilter = newsItemsAll.filter { newsItem ->
        val apiCategory = viewModel.categoryMap[selectedCategory]
        val categoryMatch = selectedCategory == "Sve" || newsItem.category == apiCategory
        val dateMatch = selectedDateRange?.let { (start, end) ->
            val itemDate = formatter.parse(newsItem.publishedDate)?.time
            itemDate != null && itemDate in (start ?: Long.MIN_VALUE)..(end ?: Long.MAX_VALUE)
        } != false
        val unwantedMatch = selectedUnwantedWords.none { unwantedWord ->
            newsItem.title.contains(unwantedWord, ignoreCase = true) ||
                    newsItem.snippet.contains(unwantedWord, ignoreCase = true)
        }
        categoryMatch && dateMatch && unwantedMatch
    }

    LaunchedEffect(viewModel.selectedCategory) {
        when (viewModel.selectedCategory) {
            "Sve" -> newsViewModel.loadAllStories()
            "Više filtera ..." -> {}
            else -> {
                viewModel.categoryMap[viewModel.selectedCategory]?.let { apiCategory ->
                    newsViewModel.loadTopStoriesByCategory(apiCategory)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 18.dp, end = 16.dp, bottom = 6.dp)
        ) {
            Text(
                text = "NewsFeed",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$selectedCategory • ${newsItemsFilter.size} vijesti",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(viewModel.categories) { category ->
                FilterChipCustom(
                    category = category,
                    selected = selectedCategory,
                    onSelected = {
                        if (it != "Više filtera ...") {
                            viewModel.selectedCategory = it
                        }
                    },
                    filterScreen = { navController.navigate("filter") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (newsItemsFilter.isEmpty() && selectedCategory != "Više filtera ...") {
            MessageCard("Nema pronađenih vijesti u kategoriji $selectedCategory")
        } else {
            key(selectedCategory) {
                NewsList(newsList = newsItemsFilter, onItemClick = { news ->
                    navController.navigate("details/${news.uuid}")
                })
            }
        }
    }
}
