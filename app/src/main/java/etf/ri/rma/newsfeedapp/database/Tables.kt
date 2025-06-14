package etf.ri.rma.newsfeedapp.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "News")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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
    val isFeatured: Boolean
)

@Entity(tableName = "Tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String
)

@Entity(
    tableName = "NewsTags",
    primaryKeys = ["newsId", "tagsId"],
    foreignKeys = [
        ForeignKey(entity = NewsEntity::class, parentColumns = ["id"], childColumns = ["newsId"]),
        ForeignKey(entity = TagEntity::class, parentColumns = ["id"], childColumns = ["tagsId"])
    ],
    indices = [Index("newsId"), Index("tagsId")]
)
data class NewsTagsCrossRef(
    val newsId: Int,
    val tagsId: Int
)


