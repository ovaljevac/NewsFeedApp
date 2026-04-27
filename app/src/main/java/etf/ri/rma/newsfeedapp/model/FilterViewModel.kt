package etf.ri.rma.newsfeedapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var selectedCategory by mutableStateOf("All")
    var selectedDateRange by mutableStateOf<Pair<Long?, Long?>?>(null)
    var unwantedWords = mutableListOf<String>()

    val categories = listOf(
        Categories("All", "filter_chip_all"),
        Categories("Politics", "filter_chip_pol"),
        Categories("Sports", "filter_chip_spo"),
        Categories("Science", "filter_chip_sci"),
        Categories("Technology", "filter_chip_tech"),
        Categories("Business", "filter_chip_bus"),
        Categories("Health", "filter_chip_hea"),
        Categories("Entertainment", "filter_chip_ent"),
        Categories("Food", "filter_chip_food"),
        Categories("Travel", "filter_chip_tra"),
        Categories("More filters ...", "filter_chip_more")
    )

    val categoryMap = mapOf(
        "Politics" to "politics",
        "Sports" to "sports",
        "Science" to "science",
        "Technology" to "tech",
        "Business" to "business",
        "Health" to "health",
        "Entertainment" to "entertainment",
        "Food" to "food",
        "Travel" to "travel"
    )
}
