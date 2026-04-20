package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import etf.ri.rma.newsfeedapp.customcomposables.DateRangePickerModal
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.customcomposables.UnwantedWordsList
import etf.ri.rma.newsfeedapp.model.Categories
import etf.ri.rma.newsfeedapp.model.NewsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    initialCategory: String,
    initialDateRange: Pair<Long?, Long?>?,
    initialUnwantedWords: List<String>,
    onBack: () -> Unit,
    onApplyFilters: (category: String, dateRange: Pair<Long?, Long?>?, unwantedWords: List<String>) -> Unit,
    categories: List<Categories>,
    viewModel: NewsViewModel = viewModel()
) {
    BackHandler { onBack() }

    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var selectedDate by remember { mutableStateOf(initialDateRange) }
    val unwantedList = remember { mutableStateListOf<String>().apply { addAll(initialUnwantedWords) } }
    var showModal by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formatted = selectedDate?.let { (start, end) ->
        if (start != null && end != null) {
            "${formatter.format(Date(start))} - ${formatter.format(Date(end))}"
        } else {
            "Nije izabran period"
        }
    } ?: "Nije izabran period"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NewsBottomBar(
                active = "explore",
                onHome = onBack,
                onExplore = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Explore",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                SearchCircleButton()
            }

            SectionTitle("Trending Now", action = "See all")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TrendingCard("Markets", "32.4K stories", MaterialTheme.colorScheme.primary)
                TrendingCard("Climate", "28.7K stories", Color(0xFF21A45D))
                TrendingCard("Artificial\nIntelligence", "21.3K stories", Color(0xFF7C4DFF))
            }

            SectionTitle("Browse Categories")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.filter { it.cat != "Vise filtera ..." }.forEach { category ->
                    FilterChipCustom(
                        category = category,
                        selected = selectedCategory,
                        onSelected = {
                            selectedCategory = it
                            categoryToApi(it)?.let { apiCategory ->
                                viewModel.loadTopStoriesPreview(apiCategory)
                            }
                        },
                        modifier = Modifier.widthIn(min = 104.dp)
                    )
                }
            }

            SectionTitle("Advanced Filters")
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("filter_daterange_button"),
                shape = RoundedCornerShape(12.dp),
                onClick = { showModal = true }
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Izaberite opseg datuma")
            }

            Text(
                text = formatted,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .testTag("filter_daterange_display")
            )

            if (showModal) {
                DateRangePickerModal(
                    onDateRangeSelected = {
                        selectedDate = it
                        showModal = false
                    },
                    onDismiss = { showModal = false }
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 18.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("filter_unwanted_input")
                        .weight(1f),
                    placeholder = { Text("Unesi rijec") }
                )
                Button(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(56.dp)
                        .testTag("filter_unwanted_add_button"),
                    onClick = {
                        val cleanText = text.trim()
                        if (
                            cleanText.isNotBlank() &&
                            unwantedList.none { it.trim().equals(cleanText, ignoreCase = true) }
                        ) {
                            unwantedList.add(cleanText)
                            text = ""
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
                OutlinedButton(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(56.dp),
                    onClick = { unwantedList.clear() }
                ) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                }
            }

            UnwantedWordsList(
                unwantedList,
                modifier = Modifier.testTag("filter_unwanted_list")
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .height(52.dp)
                        .weight(1f),
                    onClick = onBack
                ) {
                    Text("Nazad")
                }
                Button(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .height(52.dp)
                        .weight(1f)
                        .testTag("filter_apply_button"),
                    onClick = {
                        viewModel.applyFilters()
                        onApplyFilters(selectedCategory, selectedDate, unwantedList.toList())
                    }
                ) {
                    Text("Primijeni")
                }
            }
        }
    }
}

@Composable
private fun TrendingCard(title: String, count: String, color: Color) {
    Card(
        modifier = Modifier
            .width(134.dp)
            .height(132.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Surface(
                modifier = Modifier.height(38.dp).width(38.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.12f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(9.dp),
                    tint = color
                )
            }
            Text(
                text = title,
                minLines = 2,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = count,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String, action: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        if (action != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun categoryToApi(category: String): String? {
    return when (category) {
        "Politika" -> "politics"
        "Sport" -> "sports"
        "Nauka" -> "science"
        "Tehnologija" -> "tech"
        "Biznis" -> "business"
        "Zdravlje" -> "health"
        "Zabava" -> "entertainment"
        "Hrana" -> "food"
        "Putovanje" -> "travel"
        else -> null
    }
}
