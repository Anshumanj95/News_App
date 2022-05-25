package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.R
import com.example.news.databinding.ItemArticlePreviewBinding
import com.example.news.models.Article


class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(article: Article){
            binding.apply {
                Glide.with(itemView.context).load(article.urlToImage).placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image).into(ivArticleImage)
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvAuthor.text = article.author
            }
        }
    }


    private val differCallbacks= object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }

    val differ= AsyncListDiffer(this,differCallbacks)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }


    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it(article) }
        }
        holder.bind(article)
    }
    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}