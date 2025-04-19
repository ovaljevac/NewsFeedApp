package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.Categories

@Composable
fun NewsFeedScreen() {
    val newsItemsAll = NewsData.getAllNews()
    var selected by remember { mutableStateOf("Sve") }
    var currentScreen by remember { mutableStateOf("newsFeed") }
    val newsItemsFilter = if (selected == "Sve") newsItemsAll
    else newsItemsAll.filter { it.category == selected }
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
                            selected = selected,
                            onSelected = {
                                if (it == "Više filtera ...") {
                                    currentScreen = "filter"
                                } else {
                                    selected = it
                                }
                            },
                            filterScreen = { currentScreen = "filter" }
                        )
                    }
                }
                if (newsItemsFilter.isEmpty() && selected != "Više filtera ...") {
                    MessageCard("Nema pronađenih vijesti u kategoriji $selected")
                } else {
                    key(selected) {
                        NewsList(newsList = newsItemsFilter)
                    }
                }
            }
        "filter" -> {
            FilterScreen(onBack = {currentScreen = "newsFeed"})
        }
    }
}