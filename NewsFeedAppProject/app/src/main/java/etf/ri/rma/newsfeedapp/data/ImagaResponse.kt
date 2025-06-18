package etf.ri.rma.newsfeedapp.data

data class ImagaResponse(
    val result: TagResult
)

data class TagResult(
    val tags: List<TagItem>
)

data class TagItem(
    val tag: Map<String, String>,
    val confidence: Double
)
