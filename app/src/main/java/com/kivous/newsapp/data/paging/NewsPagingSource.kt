package com.kivous.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.data.network.NewsAPI
import com.kivous.newsapp.data.repositories.NewsType
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val newsAPI: NewsAPI,
    private val apiKey: String,
    private val category: String = "",
    private val searchQuery: String = "",
    private val newsType: NewsType
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> = try {
        val page = params.key ?: 1

        val response = when (newsType) {
            NewsType.BREAKING_NEWS -> {
                newsAPI.getBreakingNews(page = page, apiKey = apiKey).articles
            }

            NewsType.CATEGORY_NEWS -> {
                newsAPI.getNewsByCategory(
                    page = page, apiKey = apiKey, category = category, countryCode = "us"
                ).articles
            }

            NewsType.SCROLL_NEWS -> {
                newsAPI.getNewsByCategory(
                    page = page, apiKey = apiKey, category = category, countryCode = "us"
                ).articles
            }

            NewsType.SEARCH_NEWS -> {
                newsAPI.getSearchNews(
                    page = page, apiKey = apiKey, searchQuery = searchQuery
                ).articles
            }
        }

        LoadResult.Page(
            response,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (response.isEmpty()) null else page + 1
        )
    } catch (e: IOException) {
        LoadResult.Error(e)
    } catch (e: HttpException) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null

}
