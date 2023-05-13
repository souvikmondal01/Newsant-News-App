package com.kivous.newsapp.db

import androidx.room.TypeConverter
import com.kivous.newsapp.model.Source


class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}