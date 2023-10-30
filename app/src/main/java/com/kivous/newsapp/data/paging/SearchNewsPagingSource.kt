package com.kivous.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.data.network.NewsAPI
import retrofit2.HttpException
import java.io.IOException

class SearchNewsPagingSource(
    private val newsAPI: NewsAPI,
    private val searchQuery: String
) : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = newsAPI.getSearchNews(searchQuery = searchQuery, page = page).articles
            LoadResult.Page(
                data = response,
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