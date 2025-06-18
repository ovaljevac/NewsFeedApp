package etf.ri.rma.newsfeedapp.data

data class NewsResponse(
    val meta: Meta,
    val data: List<NewsItemDto>
)