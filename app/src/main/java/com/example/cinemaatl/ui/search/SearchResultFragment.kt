package com.example.cinemaatl.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.databinding.FragmentSearchResultBinding
import com.example.cinemaatl.ui.shared.SharedVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var binding: FragmentSearchResultBinding? = null

    private val viewModelSearch by viewModels<SearchVM>()
    private val sharedVM by activityViewModels<SharedVM>()

    private val searchAdapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding?.recyclerViewSearchResults?.adapter = searchAdapter

        searchAdapter.itemClickListener = { movie ->
            sharedVM.selectedMovie(movie,true)
            findNavController().navigate(R.id.action_searchResultFragment_to_filmDetailFragment)
        }

        binding?.tvSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    viewModelSearch.searchAllMovies(query)
                } else {
                    searchAdapter.updateData(emptyList()) // Очищаем список, если запрос пустой
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        viewModelSearch.searchResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBarSearch?.visibility = View.VISIBLE

                }

                is UIState.Success -> {
                    binding?.progressBarSearch?.visibility = View.GONE
                    val movies = state.data
                    if (movies != null) {
                        searchAdapter.updateData(movies)
                    }
                }

                is UIState.Error -> {
                    binding?.progressBarSearch?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_SHORT).show()

                }
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}



