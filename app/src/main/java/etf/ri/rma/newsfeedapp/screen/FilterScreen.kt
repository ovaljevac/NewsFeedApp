package etf.ri.rma.newsfeedapp.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.customcomposables.DateRangePickerModal
import etf.ri.rma.newsfeedapp.customcomposables.FilterChipCustom
import etf.ri.rma.newsfeedapp.model.Categories
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(onBack: () -> Unit){
    var selected by remember { mutableStateOf("Sve") }
    var aa by remember {mutableStateOf(false)}
    val categories = listOf(
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka/tehnologija", "filter_chip_sci"),
        Categories("Crna hronika", "filter_chip_none"),
    )
    var selectedDate by remember { mutableStateOf<Pair<Long?, Long?>?>(null) }
    val state = rememberDateRangePickerState()
    val dateFormat = SimpleDateFormat("DD-MM-YYYY")
    var showModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.
            fillMaxHeight()
    ) {
        Text(
                text = "KATEGORIJE: ",
                fontWeight = Bold,
                modifier = Modifier.padding(3.dp)
            )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
        ) {
            categories.forEach { category ->
                FilterChipCustom(
                    category = category,
                    selected = selected,
                    onSelected = { selected = it },
                    modifier = Modifier
                        .height(50.dp)
                        .width(110.dp)
                        .padding(1.dp)
                )
            }
        }
        Text(
            text = "DATUM: ",
            fontWeight = Bold,
            modifier = Modifier.padding(3.dp)
        )
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formatted = selectedDate?.let { (start, end) ->
            if (start != null && end != null) {
                "${formatter.format(Date(start))};${formatter.format(Date(end))}"
            } else {
                "Nije izabran period"
            }
        } ?: "Nije izabran period"

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3A3A3A), // background color
                contentColor = Color.White // color of text and icons
            ),
            modifier = Modifier
                .padding(3.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                .testTag("filter_daterange_button"),
            onClick = {
                showModal = true
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.weight(1f))
                Text("Izaberite opseg datuma")
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Text(
            text = "IZABRANI PERIOD: \n$formatted",
            fontWeight = Bold,
            modifier = Modifier
                .padding(3.dp)
                .testTag("filter_daterange_display")

        )
        if(showModal){
            DateRangePickerModal(
                onDateRangeSelected = {
                    selectedDate = it
                    showModal = false
                },
                onDismiss = { showModal = false }
            )
        }
    }
    }