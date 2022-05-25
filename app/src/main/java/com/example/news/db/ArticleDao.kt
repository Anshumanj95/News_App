package com.example.news.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.news.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article):Long

    @Query("select * from article")
    fun getAllNews(): LiveData<List<Article>>

    @Delete
    suspend fun delete(article: Article)
}