package com.example.news.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.R
import com.example.news.adapters.NewsAdapter
import com.example.news.databinding.FragmentCategoryBinding
import com.example.news.ui.NewsActivity
import com.example.news.ui.NewsViewModel
import com.example.news.util.Resource


class CategoryFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private val args:CategoryFragmentArgs by navArgs()
    private var _binding: FragmentCategoryBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        showShimmerAndHideRecycler()
        viewModel.categoryNews("in",args.category!!)
        binding.topAppBar.title=args.category
        setupRecyclerView()
        binding.game.setOnClickListener {
            val action=CategoryFragmentDirections.actionCategoryFragmentToGameFragment()
            this.findNavController().navigate(action)
        }
        binding.filter.setOnClickListener {
            val action=CategoryFragmentDirections.actionCategoryFragmentToFilterFragment()
            this.findNavController().navigate(action)
        }
        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_categoryFragment_to_articleFragment2,
                bundle)
        }

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success->{
                    hideShimmerAndShowRecycler()
                    response.data?.let {newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    hideShimmerAndShowRecycler()
                    response.message?.let {
                        Toast.makeText(activity,"An error occurred : $it", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading->{
                    showShimmerAndHideRecycler()
                }
            }
        })
    }
    private fun hideShimmerAndShowRecycler(){
        binding.shimmerLayout.visibility=View.GONE
        binding.rvCategoryNews.visibility=View.VISIBLE
    }
    private fun showShimmerAndHideRecycler() {
        binding.rvCategoryNews.visibility=View.GONE
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()


    }
    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvCategoryNews.apply {
            adapter =newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    }
