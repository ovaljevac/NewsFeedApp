package etf.ri.rma.newsfeedapp.customcomposables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.Categories


@Composable
fun FilterChipCustom(
    modifier: Modifier = Modifier,
    category: Categories,
    selected: String,
    onSelected: (String) -> Unit,
    filterScreen: () -> Unit = {},
) {
    FilterChip(
        modifier = modifier
            .padding(3.dp)
            .testTag(category.tag),
        onClick = {
            onSelected(category.cat)
            if (category.cat == "Više filtera ...") {
                filterScreen()
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