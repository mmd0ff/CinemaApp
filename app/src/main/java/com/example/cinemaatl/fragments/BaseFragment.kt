package com.example.cinemaatl.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.SearchAdapter
import com.example.cinemaatl.adapter.TopMovieAdapter
import com.example.cinemaatl.adapter.UpcomingMovieAdapter
import com.example.cinemaatl.databinding.FragmentBaseBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BaseFragment : Fragment() {
    private var binding: FragmentBaseBinding? = null
    private val viewModel: MainVM by activityViewModels()

    private var topMovieAdapter = TopMovieAdapter()


    private var adapterUpcoming = UpcomingMovieAdapter()

    private var adapterSearch = SearchAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var searchJob: Job? = null

        viewModel.getTopMovies()
        viewModel.getUpcomingMovies()

        binding?.recyclerViewTop?.adapter = topMovieAdapter
        binding?.recyclerViewUpcoming?.adapter = adapterUpcoming


        adapterUpcoming.itemClickListener = { movie ->
            viewModel.selectedMovie(movie)
            findNavController().navigate(R.id.action_baseFragment_to_filmDetailFragment)
        }

        topMovieAdapter.itemClickListener = { movie ->
            viewModel.selectedMovie(movie)
            findNavController().navigate(R.id.action_baseFragment_to_filmDetailFragment)
        }

        binding?.tvSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Реализация
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel() // Отменяем предыдущую задачу, если она существует
                searchJob = lifecycleScope.launch {

                    delay(500)

                    val query = s.toString()
                        .trim()  // Преобразует вводимый текст в строку и удаляет пробелы в начале и в конце
                    if (!query.isNullOrEmpty()) {
                        viewModel.searchAllMovies(query)
                    }
                    findNavController().navigate(R.id.action_baseFragment_to_searchResultFragment)


                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        viewModel.movies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBarTop?.visibility = View.VISIBLE
                }

                is UIState.Success -> {
                    binding?.progressBarTop?.visibility = View.GONE
                    val movies = state.data
                    if (movies != null) {
                        topMovieAdapter.updateData(movies)

                    }
                }

                is UIState.Error -> {
                    binding?.progressBarTop?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load cinema", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBarUpcoming?.visibility = View.VISIBLE
                }

                is UIState.Success -> {
                    binding?.progressBarUpcoming?.visibility = View.GONE
                    val upcomingMovies = state.data
                    if (upcomingMovies != null) {
                        adapterUpcoming.updateData(upcomingMovies)
                    }
                }

                is UIState.Error -> {
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