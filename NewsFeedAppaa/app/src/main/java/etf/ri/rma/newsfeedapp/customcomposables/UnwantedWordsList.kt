package etf.ri.rma.newsfeedapp.customcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UnwantedWordsList (
    unwantedWordsList : List<String>,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = Modifier
            .padding(horizontal = 3.dp, vertical = 5.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .background(color = Color.White)
    ){
        if (unwantedWordsList.isEmpty()) {
            Text(
                text = "Nema unesenih riječi.",
                modifier = Modifier.padding(8.dp)
            )
        } else
        LazyColumn (
            modifier = modifier
        ){
            items(unwantedWordsList) { word ->
                Column() {
                    Text(
                        text = word,
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                    )
                    Divider(
                        thickness = 1.dp,
                        modifier = Modifier
                            .padding(horizontal = 3.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}