package com.kivous.newsapp.data.network

import com.kivous.newsapp.data.model.NewsResponse
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
        apiKey: String
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
        apiKey: String
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
        apiKey: String
    ): NewsResponse

}