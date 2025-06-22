package etf.ri.rma.newsfeedapp.model

data class NewsItem(
    val uuid: String = "",
    val title: String = "",
    val description: String = "",
    val snippet: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val language: String = "",
    val publishedDate: String = "",
    val source: String = "",
    val category: String = "",
    val relevanceScore: Double? = 0.0,
    val locale: String = "",
    val isFeatured: Boolean = false,
    var tags: ArrayList<String> = arrayListOf(),
    val imageTags: List<News.TagEntity> = emptyList()
)