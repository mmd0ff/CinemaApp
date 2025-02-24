package com.example.cinemaatl.ui.topmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.databinding.FragmentTopMovieBinding
import com.example.cinemaatl.ui.base.SharedVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopMovieFragment : Fragment() {

    private var binding: FragmentTopMovieBinding? = null
    private val viewModelTop by viewModels<TopMovieVM>()
    private val sharedVM by activityViewModels<SharedVM>()
    private var topMovieAdapter = TopMovieAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTopMovieBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.topMoviesRecyclerView?.adapter = topMovieAdapter
        viewModelTop.getTopMovies()

        topMovieAdapter.itemClickListener = { movie ->
            sharedVM.selectedMovie(movie, true)
            findNavController().navigate(R.id.filmDetailFragment)
        }

        viewModelTop.movies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }

                is UIState.Success -> {
                    binding?.progressBar?.visibility = View.GONE

                    val movies = state.data
                    if (movies != null) {
                        topMovieAdapter.updateData(movies)
                    }
                }

                is UIState.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Cin", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}