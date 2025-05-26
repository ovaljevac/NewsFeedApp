package etf.ri.rma.newsfeedapp.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var selectedCategory by mutableStateOf("Sve")
    var selectedDateRange by mutableStateOf<Pair<Long?, Long?>?>(null)
    var unwantedWords = mutableListOf<String>()
}