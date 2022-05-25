package com.example.news.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.NewsApplication
import com.example.news.models.Article
import com.example.news.models.NewsResponse
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepo: NewsRepository): AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("in")
    }
    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }
    fun searchNews(searchQuery:String)=viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }
    fun categoryNews(countryCode: String,category:String)=viewModelScope.launch {
        safeCategoryNewsCall(countryCode,category)
    }
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleCategoryNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepo.insert(article)
    }
    fun getSavedNews()=newsRepo.getSavedNews()

    fun delete(article: Article)=viewModelScope.launch {
        newsRepo.delete(article)
    }

    private suspend fun  safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepo.getBreakingNews(countryCode)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network failure"))
                else -> breakingNews.postValue(Resource.Error("conversion Error"))
            }
        }
    }


    private suspend fun  safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepo.searchNews(searchQuery)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException ->searchNews.postValue(Resource.Error("Network failure"))
                else ->searchNews.postValue(Resource.Error("conversion Error"))
            }
        }
    }
    private suspend fun  safeCategoryNewsCall(countryCode: String,category: String){
        categoryNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()) {
                val response = newsRepo.categoryNews(countryCode,category)
                categoryNews.postValue(handleCategoryNewsResponse(response))
            }else{
                categoryNews.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException ->categoryNews.postValue(Resource.Error("Network failure"))
                else -> categoryNews.postValue(Resource.Error("conversion Error"))
            }
        }
    }


    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            val activeNetwork= connectivityManager.activeNetwork?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else->false
            }
        }
        return false
    }
    fun retryBreaking(){
        getBreakingNews("in")
    }
    fun retrySearch(searchQuery:String){
        searchNews(searchQuery)
    }
}