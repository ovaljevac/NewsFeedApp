package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.Categories
import java.text.SimpleDateFormat

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(){
    var selected by remember { mutableStateOf("Sve") }
    var dateRangePicker by remember {mutableStateOf(false)}
    val categories = listOf(
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka/tehnologija", "filter_chip_sci"),
        Categories("Crna hronika", "filter_chip_none"),
    )
    val state = rememberDateRangePickerState()
    val dateFormat = SimpleDateFormat("DD-MM-YYYY")
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
    ) {
        categories.forEach { category ->
            FilterChip(
                modifier = Modifier
                    .padding(3.dp)
                    .testTag(category.tag)
                    .height(50.dp)
                    .width(115.dp),
                onClick = { selected = category.cat },
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
                },colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color(0xFF3A3A3A),
                    selectedContainerColor = Color(0xFF6C4F3D),
                    labelColor = Color.White,
                    selectedLabelColor = Color.White,
                    iconColor = Color.White,
                    selectedLeadingIconColor = Color.White
                )
            )
        }
        Button(
            onClick = {dateRangePicker = true},
            modifier = Modifier
                .testTag("filter_daterange_display")
        ) {
            Text("Izaberite opseg datuma")
        }
        if(dateRangePicker) {
            DateRangePicker(
                state = state

            )
        }
        }
    }