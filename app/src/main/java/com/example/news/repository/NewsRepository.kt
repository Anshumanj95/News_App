package com.example.news.repository

import com.example.news.api.RetrofitInstance
import com.example.news.db.ArticleDatabase
import com.example.news.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode:String)=
        RetrofitInstance.api.getBreakingNews(countryCode)

    suspend fun searchNews(searchQuery:String)=
        RetrofitInstance.api.searchForNews(searchQuery)

    suspend fun categoryNews(countryCode: String,category: String)=
        RetrofitInstance.api.categoryNews(countryCode,category)

    suspend fun insert(article: Article)=db.getArticleDao().insert(article)

    fun getSavedNews()=db.getArticleDao().getAllNews()

    suspend fun delete(article: Article)=db.getArticleDao().delete(article)
}