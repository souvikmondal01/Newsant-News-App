package com.souvikmondal01.neutral.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.souvikmondal01.neutral.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles where url = :url")
    suspend fun deleteArticleByUrl(url: String)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles where url = :url")
    fun isArticleSaved(url: String): Int

    @Query("SELECT COUNT(*) FROM articles")
    fun getArticleCount(): Flow<Int>

}