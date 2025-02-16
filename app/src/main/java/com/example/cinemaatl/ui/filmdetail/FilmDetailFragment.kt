package com.example.cinemaatl.ui.filmdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentFilmDetailBinding
import com.example.cinemaatl.ui.base.SharedVM
import com.example.cinemaatl.ui.search.SearchVM
import com.example.cinemaatl.ui.topmovie.TopMovieVM
import com.example.cinemaatl.ui.upcomingmovies.UpComingMoviesVM
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint

class FilmDetailFragment : Fragment() {

    private var binding: FragmentFilmDetailBinding? = null

//    private val viewModelTop by viewModels<TopMovieVM>()
//    private val viewModelTop by activityViewModels<TopMovieVM>()
    private val sharedVM by activityViewModels<SharedVM>()
//    private val viewModelTop by viewModels<TopMovieVM>()
    private val viewModelUpComing by activityViewModels<UpComingMoviesVM>()


    private val adapterGenre = GenreAdapter()
    private val adapterActor = ActorAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentFilmDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvGenre?.adapter = adapterGenre
        binding?.recyclerviewActor?.adapter = adapterActor

//        binding?.recyclerviewActor?.adapter = adapterGenre
//        viewModelTop.getTopMovies()
//        viewModelUpComing.getUpcomingMovies()

        getBlurView()



//        viewModelUpComing.selectedMovie.observe(viewLifecycleOwner) { movie ->
//            binding?.tvDesc?.text = movie?.description.toString()
//            binding?.movieTitle?.text = movie?.name.toString()
//            binding?.tvYear?.text = movie?.year.toString()
//            binding?.duration?.text = movie?.movieLength.toString()
//            binding?.rating?.text = movie?.rating?.imdb.toString()
//
//            binding?.ivPic?.let {
//                binding?.ivPic?.context?.let { it1 ->
//                    Glide.with(it1)
//                        .load(movie?.poster?.url)
//                        .into(it)
//                }
//            }
//
//        }
        sharedVM.selectedMovie.observe(viewLifecycleOwner){ movie ->
                binding?.tvDesc?.text = movie?.description.toString()
                binding?.movieTitle?.text = movie?.name.toString()
                binding?.tvYear?.text = movie?.year.toString()
                binding?.duration?.text = movie?.movieLength.toString()
                binding?.rating?.text = movie?.rating?.imdb.toString()

                binding?.ivPic?.let {
                    binding?.ivPic?.context?.let { it1 ->
                        Glide.with(it1)
                            .load(movie?.poster?.url)
                            .into(it)
                    }
                }

            if (movie != null) {

                adapterGenre.updateGenre(movie.genres)

            }

//            if (movie?.persons.isNullOrEmpty()) {
//                adapterActor.updatePersons(movie?.persons ?: emptyList())
//                Log.d("FilmDetailFragment", "Persons list is empty or null")
//            }
//            else {
//                Log.d("FilmDetailFragment", "Persons count: ${movie?.persons?.size}")
//            }
        }
//        val movieId = viewModel.selectedMovie.value?.id
//        if(movieId != null) {
//            viewModelUpComing.getMovieId(movieId)
//        }

        binding?.btBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.btnBuyTicket?.setOnClickListener {
            findNavController().navigate(R.id.action_filmDetailFragment_to_seatsFragment)
        }


    }

    fun getBlurView() {

        val blurView = view?.findViewById<BlurView>(R.id.blurView)

        val radius = 10f
        val decorView = requireActivity().window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)
        val windowsBackground = decorView.background

        blurView?.setupWith(rootView, RenderScriptBlur(requireContext()))
            ?.setFrameClearDrawable(windowsBackground)
            ?.setBlurRadius(radius)
        binding?.blurView?.outlineProvider = ViewOutlineProvider.BACKGROUND
        binding?.blurView?.clipToOutline = true
    }

    override fun onDestroyView() {

        super.onDestroyView()
        binding = null
    }


}