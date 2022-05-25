package com.example.news.db

import androidx.room.TypeConverter
import com.example.news.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toString(name:String): Source {
        return Source(name,name)
    }
}