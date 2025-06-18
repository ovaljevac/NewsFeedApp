import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import etf.ri.rma.newsfeedapp.data.NewsItem
import etf.ri.rma.newsfeedapp.data.NewsWithTags
import etf.ri.rma.newsfeedapp.database.NewsEntity
import etf.ri.rma.newsfeedapp.database.NewsTagsCrossRef
import etf.ri.rma.newsfeedapp.database.TagEntity
import kotlin.collections.distinctBy
import kotlin.collections.map

@Dao
interface SavedNewsDAO {

    @Transaction
    suspend fun saveNews(news: NewsItem): Boolean {
        val existing = getNewsByUUID(news.uuid)
        if (existing != null) return false
        val newsEntity = NewsEntity(
            uuid = news.uuid,
            title = news.title,
            description = news.description,
            snippet = news.snippet,
            url = news.url,
            imageUrl = news.imageUrl,
            language = news.language,
            publishedDate = news.publishedDate,
            source = news.source,
            category = news.category,
            relevanceScore = news.relevanceScore,
            locale = news.locale,
            isFeatured = news.isFeatured
        )
        insertNews(newsEntity)
        return true
    }

    @Transaction
    suspend fun allNews(): List<NewsItem> {
        return getAllNewsWithTags().map { it.toNewsItem() }
    }

    @Transaction
    suspend fun getNewsWithCategory(category: String): List<NewsItem> {
        return getNewsByCategory(category).map { it.toNewsItem() }
    }

    @Transaction
    suspend fun addTags(tags: List<String>, newsId: Int): Int {
        var addedCount = 0
        for (tagValue in tags) {
            var tagId = getTagIdByValue(tagValue)
            if (tagId == null) {
                tagId = insertTag(TagEntity(value = tagValue)).toInt()
                if (tagId != -1) addedCount++
            }
            insertNewsTagCrossRef(NewsTagsCrossRef(newsId, tagId))
        }
        return addedCount
    }

    @Query("""
        SELECT Tags.value FROM Tags
        INNER JOIN NewsTags ON Tags.id = NewsTags.tagsId
        WHERE NewsTags.newsId = :newsId
    """)
    suspend fun getTags(newsId: Int): List<String>

    @Transaction
    suspend fun getSimilarNews(tags: List<String>): List<NewsItem> {
        val tagIds = getTagIdsByValues(tags)
        val relatedNews = getNewsWithAnyTag(tagIds)
        return relatedNews
            .distinctBy { it.news.uuid }
            .sortedByDescending { it.news.publishedDate }
            .map { it.toNewsItem() }
    }

    private fun NewsWithTags.toNewsItem(): NewsItem {
        return NewsItem(
            uuid = news.uuid,
            title = news.title,
            description = news.description,
            snippet = news.snippet,
            url = news.url,
            imageUrl = news.imageUrl,
            language = news.language,
            publishedDate = news.publishedDate,
            source = news.source,
            category = news.category,
            relevanceScore = news.relevanceScore,
            locale = news.locale,
            isFeatured = news.isFeatured,
            imageTags = ArrayList(tags.map { it.value })
        )
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsTagCrossRef(ref: NewsTagsCrossRef)

    @Query("SELECT * FROM News WHERE uuid = :uuid LIMIT 1")
    suspend fun getNewsByUUID(uuid: String): NewsEntity?

    @Transaction
    @Query("SELECT * FROM News")
    suspend fun getAllNewsWithTags(): List<NewsWithTags>

    @Transaction
    @Query("SELECT * FROM News WHERE category = :category")
    suspend fun getNewsByCategory(category: String): List<NewsWithTags>

    @Query("SELECT id FROM Tags WHERE value = :value LIMIT 1")
    suspend fun getTagIdByValue(value: String): Int?

    @Query("SELECT id FROM Tags WHERE value IN (:tags)")
    suspend fun getTagIdsByValues(tags: List<String>): List<Int>

    @Query("SELECT id FROM News WHERE uuid = :uuid LIMIT 1")
    fun getIdByUuid(uuid: String): Int?

    @Transaction
    @Query("""
        SELECT * FROM News
        INNER JOIN NewsTags ON News.id = NewsTags.newsId
        WHERE NewsTags.tagsId IN (:tagIds)
    """)
    suspend fun getNewsWithAnyTag(tagIds: List<Int>): List<NewsWithTags>
}
