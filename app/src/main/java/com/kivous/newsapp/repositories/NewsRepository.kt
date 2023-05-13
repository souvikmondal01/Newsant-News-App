package com.kivous.newsapp.repositories

import com.kivous.newsapp.db.ArticleDatabase
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.network.NewsAPI
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
}