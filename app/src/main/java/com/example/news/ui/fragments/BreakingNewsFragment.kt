package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentBreakingNewsBinding
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.Resource

class BreakingNewsFragment:Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private var _binding: FragmentBreakingNewsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentBreakingNewsBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()
        binding.game.setOnClickListener {
            val action=BreakingNewsFragmentDirections.actionBreakingNewsFragmentToGameFragment()
            this.findNavController().navigate(action)
        }
        binding.filter.setOnClickListener {
            val action=BreakingNewsFragmentDirections.actionBreakingNewsFragmentToFilterFragment()
            this.findNavController().navigate(action)
        }
        binding.retry.setOnClickListener {
            hide()
            viewModel.retryBreaking()
        }
        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
            bundle)
        }
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.searchNews -> {
                    findNavController().navigate(R.id.action_breakingNewsFragment_to_searchNewsFragment)
                    true
                }
                R.id.savedNews -> {
                    findNavController().navigate(R.id.action_breakingNewsFragment_to_savedNewsFragment)
                    true
                }
                else -> false
            }
        }
        updateData()
    }
    private fun updateData(){
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success->{
                    hideShimmerAndShowRecycler()
                    hide()
                    response.data?.let {newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideShimmerAndShowRecycler()
                    response.message?.let {
                        binding.error.visibility=View.VISIBLE
                        binding.retry.visibility=View.VISIBLE
                        binding.msg.visibility=View.VISIBLE
                        binding.some.visibility=View.VISIBLE
                    }
                }
                is Resource.Loading->{
                    showShimmerAndHideRecycler()
                }
            }
        })
    }
    private fun hide(){
        binding.error.visibility=View.INVISIBLE
        binding.retry.visibility=View.INVISIBLE
        binding.msg.visibility=View.INVISIBLE
        binding.some.visibility=View.INVISIBLE
    }
    private fun hideShimmerAndShowRecycler(){
        binding.shimmerLayout.visibility=View.GONE
        binding.rvBreakingNews.visibility=View.VISIBLE
    }

    private fun showShimmerAndHideRecycler() {
        binding.rvBreakingNews.visibility=View.GONE
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()
    }
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter =newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}