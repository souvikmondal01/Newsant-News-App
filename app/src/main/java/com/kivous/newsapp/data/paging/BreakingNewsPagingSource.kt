package com.kivous.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.data.network.NewsAPI
import retrofit2.HttpException
import java.io.IOException

class BreakingNewsPagingSource(
    private val newsAPI: NewsAPI,
    private val apiKey: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = newsAPI.getBreakingNews(page = page, apiKey = apiKey).articles
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
    }
}