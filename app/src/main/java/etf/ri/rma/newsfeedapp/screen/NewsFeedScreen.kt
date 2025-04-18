package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.Categories

@Composable
fun NewsFeedScreen() {
        val newsItemsAll = NewsData.getAllNews()
        var selected by remember { mutableStateOf("Sve") }
        var filterScreen by remember {mutableStateOf(false)}
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

    Column(modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
            .background(color = Color(0xFFD2B48C))) {
        if (!filterScreen) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(categories) { category ->
                    FilterChip(
                        modifier = Modifier
                            .padding(3.dp)
                            .testTag(category.tag),
                        onClick = {
                            selected = category.cat
                            if (selected == "Više filtera ...") {
                                filterScreen = true
                            }
                        },
                        label = {
                            Text(category.cat)
                        },
                        selected = selected == category.cat,
                        leadingIcon = if (selected == category.cat) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }

                        } else {
                            null
                        }, colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF3A3A3A),
                            selectedContainerColor = Color(0xFF6C4F3D),
                            labelColor = Color.White,
                            selectedLabelColor = Color.White,
                            iconColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }
        }

            if(filterScreen){
                FilterScreen()
            }
            else if(newsItemsFilter.isEmpty() && selected != "Više filtera ..."){
                MessageCard("Nema pronađenih vijesti u kategoriji $selected")
            } else {
                key(selected) {
                    NewsList(newsList = newsItemsFilter)
                }
            }

        }
    }