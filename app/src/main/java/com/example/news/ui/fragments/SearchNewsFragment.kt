package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentSearchNewsBinding
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG="SearchNewsFragment"
    private var _binding: FragmentSearchNewsBinding?=null
    private val binding get() = _binding!!
    private lateinit var searchQuery:String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSearchNewsBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()
        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle)
        }
        binding.retry.setOnClickListener {
            hide()
            viewModel.retrySearch(searchQuery)
        }
        var job:Job?=null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        showShimmerAndHideRecycler()
                        searchQuery=editable.toString()
                        viewModel.searchNews(searchQuery)
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideShimmerAndShowRecycler()
                    hide()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)


                    }
                }
                is Resource.Error -> {
                    hideShimmerAndShowRecycler()
                    response.message?.let {
                        binding.error.visibility=View.VISIBLE
                        binding.retry.visibility=View.VISIBLE
                        binding.msg.visibility=View.VISIBLE
                        binding.some.visibility=View.VISIBLE
                    }
                }
                is Resource.Loading -> {
                    showShimmerAndHideRecycler()
                }
            }
        }

    }
    private fun hide(){
        binding.error.visibility=View.INVISIBLE
        binding.retry.visibility=View.INVISIBLE
        binding.msg.visibility=View.INVISIBLE
        binding.some.visibility=View.INVISIBLE
    }
    private fun hideShimmerAndShowRecycler(){
        binding.shimmerLayout.visibility=View.GONE
        binding.rvSearchNews.visibility=View.VISIBLE
    }

    private fun showShimmerAndHideRecycler() {
        binding.rvSearchNews.visibility=View.GONE
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
    }
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
