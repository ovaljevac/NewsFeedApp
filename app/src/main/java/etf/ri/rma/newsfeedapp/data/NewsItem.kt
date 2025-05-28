package etf.ri.rma.newsfeedapp.data

data class NewsItem(
    val uuid: String,
    val title: String,
    val description: String,
    val snippet: String,
    val url: String,
    val imageUrl: String,
    val language: String,
    val publishedDate: String,
    val source: String,
    val category: String,
    val relevanceScore: Double?,
    val locale: String,
    val isFeatured: Boolean,
    var imageTags: ArrayList<String> = arrayListOf()
)