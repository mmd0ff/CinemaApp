package com.example.cinemaatl.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.TopMovieAdapter
import com.example.cinemaatl.adapter.UpcomingMovieAdapter
import com.example.cinemaatl.databinding.FragmentBaseBinding
import com.example.cinemaatl.model.Doc
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseFragment : Fragment() {
    private lateinit var binding: FragmentBaseBinding
    private val viewModel: MainVM by activityViewModels()

    private var topMovieAdapter = TopMovieAdapter()


    private var adapterUpcoming = UpcomingMovieAdapter()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getTopMovies()
        viewModel.getUpcomingMovies()

        binding.recyclerViewTop.adapter = topMovieAdapter
        binding.recyclerViewUpcoming.adapter = adapterUpcoming

        adapterUpcoming.itemClickListener = { movie ->
            viewModel.selectedMovie(movie)
            findNavController().navigate(R.id.action_baseFragment_to_filmDetailFragment)
        }

        topMovieAdapter.itemClickListener = {movie ->
            viewModel.selectedMovie(movie)
            findNavController().navigate(R.id.action_baseFragment_to_filmDetailFragment)
        }



        viewModel.movies.observe(viewLifecycleOwner){state ->
            when(state){
                is UIState.Loading ->{
                    binding.progressBarTop.visibility = View.VISIBLE
                }
                is UIState.Success ->{
                    binding.progressBarTop.visibility = View.GONE
                    val movies = state.data
                    if(movies !=null){
                        topMovieAdapter.updateData(movies)

                    }
                }
                is UIState.Error ->{
                    binding.progressBarTop.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load cinema", Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner){state ->
            when(state){
                is UIState.Loading ->{
                    binding.progressBarUpcoming.visibility = View.VISIBLE
                }
                is UIState.Success ->{
                    binding.progressBarUpcoming.visibility = View.GONE
                    val upcomingMovies = state.data
                    if(upcomingMovies != null){
                        adapterUpcoming.updateData(upcomingMovies)
                    }
                }
                is UIState.Error ->{
                    Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_SHORT).show()

                }
            }

        }


    }




}