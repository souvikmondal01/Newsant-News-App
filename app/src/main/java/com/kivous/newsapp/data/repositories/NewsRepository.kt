package com.kivous.newsapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.data.network.NewsAPI
import com.kivous.newsapp.data.paging.BreakingNewsPagingSource
import com.kivous.newsapp.data.paging.CategoryNewsPagingSource
import com.kivous.newsapp.data.paging.SearchNewsPagingSource
import com.kivous.newsapp.db.ArticleDatabase
import com.kivous.newsapp.utils.Constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsAPI, private val db: ArticleDatabase
) {
    suspend fun insertArticle(article: Article) = db.getArticleDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    fun deleteArticleByUrl(url: String) = CoroutineScope(Dispatchers.IO).launch {
        db.getArticleDao().deleteArticleByUrl(url)
    }

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    fun isArticleExist(url: String) = db.getArticleDao().isArticleExist(url)

    fun isArticleListEmpty(): Flow<Boolean> = db.getArticleDao().getArticleCount().map {
        it == 0
    }

    fun getBreakingNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig()
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        BreakingNewsPagingSource(newsApi)
    }.flow

    fun getCategoryNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), category: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        CategoryNewsPagingSource(newsApi, "in", category)
    }.flow

    fun getScrollNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), category: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        CategoryNewsPagingSource(newsApi, "us", category)
    }.flow

    fun getSearchNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), searchQuery: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        SearchNewsPagingSource(newsApi, searchQuery)
    }.flow

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = false
        )
    }

}
