package com.example.cinemaatl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.SearchAdapter
import com.example.cinemaatl.databinding.FragmentSearchResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private val viewModel: MainVM by activityViewModels()
    private val searchAdapter = SearchAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewSearchResults.adapter = searchAdapter

        searchAdapter.itemClickListener = { movie ->
            viewModel.selectedMovie(movie)
            findNavController().navigate(R.id.action_searchResultFragment_to_filmDetailFragment)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner){query ->
            viewModel.searchAllMovies(query)
        }

        viewModel.searchResult.observe(viewLifecycleOwner){state ->
            when(state){
                is UIState.Loading ->{
                    binding.progressBarSearch.visibility = View.VISIBLE

                }
                is UIState.Success ->{
                    binding.progressBarSearch.visibility = View.GONE
                    val movies = state.data
                    if(movies != null){
                        searchAdapter.updateData(movies)
                    }
                }
                is UIState.Error ->{
                    binding.progressBarSearch.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_SHORT).show()

                }
            }


        }

    }
}



