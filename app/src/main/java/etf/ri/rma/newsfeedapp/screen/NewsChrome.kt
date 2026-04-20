package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SearchCircleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NewsBottomBar(
    active: String,
    onHome: () -> Unit,
    onExplore: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(72.dp)
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomBarItem(
                label = "Home",
                icon = Icons.Filled.Home,
                selected = active == "home",
                onClick = onHome
            )
            BottomBarItem(
                label = "Explore",
                icon = Icons.Filled.Search,
                selected = active == "explore",
                onClick = onExplore
            )
            BottomBarItem(
                label = "Saved",
                icon = Icons.Filled.Favorite,
                selected = false,
                onClick = {}
            )
            BottomBarItem(
                label = "Profile",
                icon = Icons.Filled.AccountCircle,
                selected = false,
                onClick = {}
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Surface(
            modifier = Modifier.size(34.dp),
            shape = RoundedCornerShape(17.dp),
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.padding(8.dp),
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else tint
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = tint
        )
    }
}

fun readableCategory(category: String): String {
    return when (category) {
        "politics" -> "Politics"
        "sports" -> "Sports"
        "science" -> "Science"
        "tech" -> "Tech"
        "business" -> "Business"
        "health" -> "Health"
        "entertainment" -> "Entertainment"
        "food" -> "Food"
        "travel" -> "Travel"
        else -> category.replaceFirstChar { it.uppercase() }
    }
}
