package com.example.news.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.news.R
import com.example.news.adapters.adapter
import com.example.news.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {
    private var list =ArrayList<String>(listOf("Business","Entertainment","General","Health","Science","Sports","Technology"))
    private var image=ArrayList<Int>(listOf(R.drawable.bus,R.drawable.en,R.drawable.gn,R.drawable.health,R.drawable.science,R.drawable.sports,R.drawable.tech))

    private var _binding: FragmentFilterBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentFilterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter= adapter(requireActivity(),list,image)
        binding.filters.adapter=adapter

        binding.filters.setOnItemClickListener { adapterView, view, position, _ ->
            val itemAtPos=adapterView.getItemAtPosition(position)
            val action=FilterFragmentDirections.actionFilterFragmentToCategoryFragment3(itemAtPos.toString())
            view.findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }









}