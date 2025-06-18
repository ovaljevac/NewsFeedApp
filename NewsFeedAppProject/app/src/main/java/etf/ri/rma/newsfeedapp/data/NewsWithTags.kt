package etf.ri.rma.newsfeedapp.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import etf.ri.rma.newsfeedapp.database.NewsEntity
import etf.ri.rma.newsfeedapp.database.NewsTagsCrossRef
import etf.ri.rma.newsfeedapp.database.TagEntity

data class NewsWithTags(
    @Embedded val news: NewsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = NewsTagsCrossRef::class,
            parentColumn = "newsId",
            entityColumn = "tagsId"
        )
    )
    val tags: List<TagEntity>
)
