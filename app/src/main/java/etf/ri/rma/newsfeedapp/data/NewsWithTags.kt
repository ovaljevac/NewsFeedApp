package etf.ri.rma.newsfeedapp.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import etf.ri.rma.newsfeedapp.model.News.NewsEntity
import etf.ri.rma.newsfeedapp.model.News.NewsTagsCrossRef
import etf.ri.rma.newsfeedapp.model.News.TagEntity

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
