package com.kivous.newsapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kivous.newsapp.data.model.Article
import com.kivous.newsapp.data.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    fun getBreakingNews(): Flow<PagingData<Article>> =
        newsRepository.getBreakingNewsFlow().cachedIn(viewModelScope)

    fun getCategoryNews(category: String): Flow<PagingData<Article>> =
        newsRepository.getCategoryNewsFlow(category = category).cachedIn(viewModelScope)

    fun getScrollNews(category: String): Flow<PagingData<Article>> =
        newsRepository.getScrollNewsFlow(category = category).cachedIn(viewModelScope)

    fun getSearchNews(searchQuery: String): Flow<PagingData<Article>> =
        newsRepository.getSearchNewsFlow(searchQuery = searchQuery).cachedIn(viewModelScope)

    fun insertArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun deleteArticleByUrl(url: String) = viewModelScope.launch {
        newsRepository.deleteArticleByUrl(url)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun isArticleExist(url: String) = newsRepository.isArticleExist(url)

    fun isArticleListEmpty(): Flow<Boolean> = newsRepository.isArticleListEmpty()

}