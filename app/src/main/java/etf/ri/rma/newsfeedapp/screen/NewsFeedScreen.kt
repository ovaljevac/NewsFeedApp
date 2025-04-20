package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.Categories
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsFeedScreen() {
    val newsItemsAll = NewsData.getAllNews()
    var selectedCategory by remember { mutableStateOf("Sve") }
    var currentScreen by remember { mutableStateOf("newsFeed") }
    var selectedDateRange by remember { mutableStateOf<Pair<Long?, Long?>?>(null) }
    val selectedUnwantedWords = remember { mutableStateListOf<String>() }
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    var selectedNewsId by remember { mutableStateOf<String?>(null) }
    val newsItemsFilter = newsItemsAll.filter { newsItem ->
        val categoryMatch = selectedCategory == "Sve" || newsItem.category == selectedCategory
        val dateMatch = selectedDateRange?.let { (start, end) ->
            val itemDate = try {
                formatter.parse(newsItem.publishedDate)?.time
            } catch (e: Exception) {
                null
            }
            itemDate != null && itemDate in (start ?: Long.MIN_VALUE)..(end ?: Long.MAX_VALUE)
        } ?: true
        val unwantedMatch = selectedUnwantedWords.none { unwantedWord ->
            newsItem.title.contains(unwantedWord, ignoreCase = true) ||
                    newsItem.snippet.contains(unwantedWord, ignoreCase = true)
        }
        categoryMatch && dateMatch && unwantedMatch
    }
    val categories = listOf(
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka/tehnologija", "filter_chip_sci"),
        Categories("Crna hronika", "filter_chip_none"),
        Categories("Više filtera ...", "filter_chip_more")
    )
    when (currentScreen) {
        "newsFeed" ->
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
                                    currentScreen = "filter"
                                } else {
                                    selectedCategory = it
                                }
                            },
                            filterScreen = { currentScreen = "filter" }
                        )
                    }
                }
                if (newsItemsFilter.isEmpty() && selectedCategory != "Više filtera ...") {
                    MessageCard("Nema pronađenih vijesti u kategoriji $selectedCategory")
                } else {
                    key(selectedCategory) {
                        NewsList(newsList = newsItemsFilter, onItemClick = { news ->
                            selectedNewsId  = news.id
                            currentScreen = "details"
                        })
                    }
                }
            }
        "filter" -> {
            FilterScreen(
                initialCategory = selectedCategory,
                initialDateRange = selectedDateRange,
                initialUnwantedWords = selectedUnwantedWords.toList(),
                onApplyFilters = { category, dateRange, unwantedWords ->
                    selectedCategory = category
                    selectedDateRange = dateRange
                    selectedUnwantedWords.clear()
                    selectedUnwantedWords.addAll(unwantedWords)
                    currentScreen = "newsFeed"
                } ,
                onBack = { currentScreen = "newsFeed" }
            )
        }
        "details" ->{
            val selectedNews = newsItemsAll.find { it.id == selectedNewsId }!!
            NewsDetailsScreen(
                news = selectedNews,
                onBack = {currentScreen = "newsFeed"},
                onNewsSelected = {relatedNews ->
                    selectedNewsId = relatedNews.id
                }
            )
        }
    }
}