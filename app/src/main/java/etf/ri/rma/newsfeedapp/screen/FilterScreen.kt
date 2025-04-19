package etf.ri.rma.newsfeedapp.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
    val categories = listOf(
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka/tehnologija", "filter_chip_sci"),
        Categories("Crna hronika", "filter_chip_none"),
    )
    var selectedDate by remember { mutableStateOf<Pair<Long?, Long?>?>(null) }
    var showModal by remember { mutableStateOf(false) }
    val unwantedList = remember { mutableStateListOf<String>() }
    var text by remember { mutableStateOf("")}
    var backButton by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Text(
                text = "KATEGORIJE: ",
                fontWeight = Bold,
                modifier = Modifier.padding(3.dp)
            )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp, horizontal = 5.dp)
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
                containerColor = Color(0xFF3A3A3A),
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(vertical = 3.dp, horizontal = 5.dp)
                .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                .testTag("filter_daterange_button")
                .padding(horizontal = 3.dp),
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
            text = "IZABRANI PERIOD: ",
            fontWeight = Bold,
            modifier = Modifier
                .padding(3.dp)
        )
        Text(
            text = "$formatted",
            fontWeight = Bold,
            modifier = Modifier
                .padding(3.dp)
                .testTag("filter_daterange_display")
                .align(alignment = Alignment.CenterHorizontally)
                .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp)
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
        Text(
            text = "UNESITE ZABRANJENE RIJEČI: ",
            fontWeight = Bold,
            modifier = Modifier
                .padding(3.dp)

        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .testTag("filter_unwanted_input")
                    .padding(vertical = 3.dp, horizontal = 5.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                    .height(56.dp)
                    .weight(1f)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A3A3A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 5.dp)
                    .height(53.dp),
                onClick = {
                    if(text.isNotBlank()){
                    unwantedList.add(text)
                    text = ""}
                }
            ) {
                Text("Dodaj")
            }
        }
        UnwantedWordsList(unwantedList)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.Bottom,
             modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues())

        ){
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3A3A3A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 5.dp)
                    .height(53.dp),
                onClick = {
                    backButton = !backButton
                    onBack()
                }
            ) {
                Text("Nazad")
            }
        }
    }
    }