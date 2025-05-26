package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.Categories
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
    var selectedCategory = viewModel.selectedCategory
    var selectedDateRange = viewModel.selectedDateRange
    val selectedUnwantedWords = viewModel.unwantedWords
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val newsItemsFilter = newsItemsAll.filter { newsItem ->
        val categoryMatch = selectedCategory == "Sve" || newsItem.category == selectedCategory
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
    val categories = listOf(
        Categories("Više filtera ...", "filter_chip_more"),
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka/tehnologija", "filter_chip_sci"),
        Categories("Crna hronika", "filter_chip_none"),
    )
    LaunchedEffect(selectedCategory) {
        newsViewModel.loadTopStories(selectedCategory)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(categories) { category ->
                FilterChipCustom(
                    category = category,
                    selected = selectedCategory,
                    onSelected = {
                        if (it == "Više filtera ...") {
                            navController.navigate("filter")
                        } else {
                            viewModel.selectedCategory = it
                        }
                                 },
                    filterScreen = { navController.navigate("filter") }
                )
            }
        }
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