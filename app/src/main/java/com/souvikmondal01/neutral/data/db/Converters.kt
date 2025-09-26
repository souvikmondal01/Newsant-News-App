package com.souvikmondal01.neutral.data.db

import androidx.room.TypeConverter
import com.souvikmondal01.neutral.data.model.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? = source.name

    @TypeConverter
    fun toSource(name: String): Source = Source(name, name)

}