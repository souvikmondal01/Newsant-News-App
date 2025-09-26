package com.souvikmondal01.neutral.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.souvikmondal01.neutral.data.network.NewsAPI
import com.souvikmondal01.neutral.data.db.ArticleDatabase
import com.souvikmondal01.neutral.utils.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideNewsApi(): NewsAPI = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
        .create(NewsAPI::class.java)

    @Provides
    @Singleton
    fun provideArticleDB(@ApplicationContext context: Context): ArticleDatabase =
        Room.databaseBuilder(
            context, ArticleDatabase::class.java, "article_db.db"
        ).build()

    @Singleton
    @Provides
    fun providesConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Singleton
    @Provides
    fun providesApiKey(): Deferred<String> = CoroutineScope(Dispatchers.IO).async {
        Firebase.firestore.collection("newsant")
            .document("api_key").get().await().get("key") as String
    }

}
