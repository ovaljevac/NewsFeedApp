package etf.ri.rma.newsfeedapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var selectedCategory by mutableStateOf("Sve")
    var selectedDateRange by mutableStateOf<Pair<Long?, Long?>?>(null)
    var unwantedWords = mutableListOf<String>()
    val categories = listOf(
        Categories("Više filtera ...", "filter_chip_more"),
        Categories("Sve", "filter_chip_all"),
        Categories("Politika", "filter_chip_pol"),
        Categories("Sport", "filter_chip_spo"),
        Categories("Nauka", "filter_chip_sci"),
        Categories("Tehnologija", "filter_chip_tech"),
        Categories("Crna hronika", "filter_chip_none"),
        Categories("Biznis", "filter_chip_bus"),
        Categories("Zdravlje", "filter_chip_hea"),
        Categories("Zabava", "filter_chip_ent"),
        Categories("Hrana", "filter_chip_food"),
        Categories("Putovanje", "filter_chip_tra")
    )
    val categoryMap = mapOf(
        "Politika" to "politics",
        "Sport" to "sports",
        "Nauka" to "science",
        "Tehnologija" to "tech",
        "Biznis" to "business",
        "Zdravlje" to "health",
        "Zabava" to "entertainment",
        "Hrana" to "food",
        "Putovanje" to "travel"
    )
}