package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
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
    BackHandler {
        onBack()
    }
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

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Filteri",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Prilagodi kategorije, datum i neželjene riječi.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        SectionTitle("Kategorije")
        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.forEach { category ->
                FilterChipCustom(
                    category = category,
                    selected = selectedCategory,
                    onSelected = {
                        selectedCategory = it
                        viewModel.loadTopStoriesPreview(it)
                    },
                    modifier = Modifier.widthIn(min = 96.dp)
                )
            }
        }

        SectionDivider()
        SectionTitle("Datum")
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("filter_daterange_button"),
            shape = RoundedCornerShape(8.dp),
            onClick = { showModal = true }
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
            Spacer(modifier = Modifier.weight(1f))
            Text("Izaberite opseg datuma")
            Spacer(modifier = Modifier.weight(1f))
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

        SectionDivider()
        SectionTitle("Zabranjene riječi")
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier
                    .testTag("filter_unwanted_input")
                    .weight(1f),
                placeholder = { Text("Unesi riječ") }
            )
            Button(
                shape = RoundedCornerShape(8.dp),
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
                shape = RoundedCornerShape(8.dp),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .height(52.dp)
                    .weight(1f),
                onClick = onBack
            ) {
                Text("Nazad")
            }
            Button(
                shape = RoundedCornerShape(8.dp),
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

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 22.dp, bottom = 8.dp)
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(top = 18.dp),
        color = MaterialTheme.colorScheme.outline
    )
}
