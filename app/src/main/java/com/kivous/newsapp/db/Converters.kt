package com.kivous.newsapp.db

import androidx.room.TypeConverter
import com.kivous.newsapp.data.model.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)

}