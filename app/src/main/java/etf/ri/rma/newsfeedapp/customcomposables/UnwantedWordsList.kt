package etf.ri.rma.newsfeedapp.customcomposables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UnwantedWordsList(
    unwantedWordsList: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 5.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        if (unwantedWordsList.isEmpty()) {
            Text(
                text = "No words added.",
                modifier = Modifier.padding(14.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            LazyColumn(modifier = modifier) {
                items(unwantedWordsList) { word ->
                    Column {
                        Text(
                            text = word,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}
