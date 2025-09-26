package com.souvikmondal01.neutral.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.souvikmondal01.neutral.data.model.Article
import com.souvikmondal01.neutral.data.network.NewsAPI
import com.souvikmondal01.neutral.data.paging.NewsPagingSource
import com.souvikmondal01.neutral.data.db.ArticleDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApi: NewsAPI,
    private val db: ArticleDatabase,
) {
    suspend fun insertArticle(article: Article) = db.getArticleDao().insertArticle(article)

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

    suspend fun deleteArticleByUrl(url: String) = db.getArticleDao().deleteArticleByUrl(url)

    fun getSavedArticles() = db.getArticleDao().getAllArticles()

    fun isArticleAlreadySaved(url: String) = db.getArticleDao().isArticleSaved(url) != 0

    fun isArticleListEmpty(): Flow<Boolean> = db.getArticleDao().getArticleCount().map { it == 0 }

    fun getBreakingNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), apiKey: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        NewsPagingSource(newsAPI = newsApi, apiKey = apiKey, newsType = NewsType.BREAKING_NEWS)
    }.flow

    fun getCategoryNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), category: String, apiKey: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        NewsPagingSource(
            newsAPI = newsApi,
            apiKey = apiKey,
            newsType = NewsType.CATEGORY_NEWS,
            category = category
        )
    }.flow

    fun getScrollNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), category: String, apiKey: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        NewsPagingSource(
            newsAPI = newsApi, apiKey = apiKey, newsType = NewsType.SCROLL_NEWS, category = category
        )
    }.flow

    fun getSearchNewsFlow(
        pagingConfig: PagingConfig = getDefaultPageConfig(), searchQuery: String, apiKey: String
    ): Flow<PagingData<Article>> = Pager(pagingConfig) {
        NewsPagingSource(
            newsAPI = newsApi,
            apiKey = apiKey,
            newsType = NewsType.SEARCH_NEWS,
            searchQuery = searchQuery
        )
    }.flow

    private fun getDefaultPageConfig(): PagingConfig = PagingConfig(
        pageSize = 20, enablePlaceholders = false
    )

}

enum class NewsType {
    BREAKING_NEWS, CATEGORY_NEWS, SCROLL_NEWS, SEARCH_NEWS
}