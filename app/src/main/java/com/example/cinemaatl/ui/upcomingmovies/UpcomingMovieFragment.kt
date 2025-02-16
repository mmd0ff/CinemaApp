package com.example.cinemaatl.ui.upcomingmovies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.databinding.FragmentUpcomingMovieBinding
import com.example.cinemaatl.ui.base.SharedVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMovieFragment : Fragment() {

    private var binding: FragmentUpcomingMovieBinding? = null
    private val viewModelUpComing  by viewModels<UpComingMoviesVM>()
    private val sharedVM by activityViewModels<SharedVM>()

//    private val viewModelUpComing by activityViewModels<UpComingMoviesVM>()
    private var upcomingMovieAdapter = UpcomingMovieAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingMovieBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.onComingRecyclerView?.adapter = upcomingMovieAdapter
        viewModelUpComing.getUpcomingMovies()

        upcomingMovieAdapter.itemClickListener = { movie ->
            sharedVM.selectedMovie(movie)
            viewModelUpComing.setButtonState(false)

            findNavController().navigate(R.id.filmDetailFragment)
        }


        viewModelUpComing.upcomingMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBar?.visibility = View.VISIBLE
                }

                is UIState.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val movies = state.data
                    upcomingMovieAdapter.updateData(movies)
                }

                is UIState.Error -> {
                    binding?.progressBar?.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to Load Cinema", Toast.LENGTH_SHORT)
                        .show()


                }
            }

        }

    }


}

