package com.kivous.newsapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kivous.newsapp.model.Article
import com.kivous.newsapp.network.NewsAPI

class NewsPagingSource(
    private val api: NewsAPI,
    private val countryCode: String,
    private val category: String
) :
    PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val position = params.key ?: 1
            val response = api.getNewsByCategory(position, countryCode, category)

            LoadResult.Page(
                data = response.body()!!.articles,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (position == response.body()!!.totalResults) null else position + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }

}