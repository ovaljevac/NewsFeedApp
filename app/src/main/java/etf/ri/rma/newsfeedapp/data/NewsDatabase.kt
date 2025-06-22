package etf.ri.rma.newsfeedapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import etf.ri.rma.newsfeedapp.model.News.NewsEntity
import etf.ri.rma.newsfeedapp.model.News.NewsTagsCrossRef
import etf.ri.rma.newsfeedapp.model.News.TagEntity

@Database(
    entities = [NewsEntity::class, TagEntity::class, NewsTagsCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun savedNewsDAO(): SavedNewsDAO

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                                context.applicationContext,
                                NewsDatabase::class.java,
                                DATABASE_NAME
                            ).fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        const val DATABASE_NAME = "news-db"
    }
}