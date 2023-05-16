package com.kivous.newsapp.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.kivous.newsapp.db.ArticleDatabase
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.network.NewsAPI
import com.kivous.newsapp.paging.NewsPagingSource
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsAPI,
    private val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchForNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun getCategoryNews(category: String) = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { NewsPagingSource(api, category) }
    ).liveData

}