package com.kivous.newsapp.data.network

import com.kivous.newsapp.data.model.NewsResponse
import com.kivous.newsapp.utils.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("page")
        page: Int,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        page: Int,
        @Query("language")
        language: String = "en",
        @Query("sortBy")
        sortBy: String = "popularity",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse


    @GET("v2/top-headlines")
    suspend fun getNewsByCategory(
        @Query("page")
        page: Int,
        @Query("country")
        countryCode: String,
        @Query("category")
        category: String,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): NewsResponse

}