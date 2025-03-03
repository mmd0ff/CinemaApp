package com.example.cinemaatl.ui.filmdetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.databinding.FragmentFilmDetailBinding
import com.example.cinemaatl.ui.shared.SharedVM
import dagger.hilt.android.AndroidEntryPoint
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

@AndroidEntryPoint

class FilmDetailFragment : Fragment() {

    private var binding: FragmentFilmDetailBinding? = null
    private val sharedVM by activityViewModels<SharedVM>()

    private val adapterGenre = GenreAdapter()

    private val adapterActor = ActorAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvGenre?.adapter = adapterGenre
        binding?.recyclerviewActor?.adapter = adapterActor

        getBlurView()


        sharedVM.selectedMovie.observe(viewLifecycleOwner) { movie ->
            if (movie != null) {
                binding?.tvDesc?.text = movie.description ?: "No description"
                binding?.movieTitle?.text = movie.name ?: "Unknown"
                binding?.tvYear?.text = movie.year ?: "N/A"
                binding?.duration?.text = movie.movieLength+getString(R.string.min)
//                binding?.duration?.text = movie.movieLength ?: "N/A"
                binding?.rating?.text = movie.rating?.imdb ?: "N/A"


                binding?.ivPic?.let { it ->
                    Glide.with(it.context)
                        .load(movie.poster?.url)
                        .into(it)

                }
                movie.genres?.let { adapterGenre.updateGenre(it) }

            }
        }

        sharedVM.isMovieTop.observe(viewLifecycleOwner) { isTopMovie ->
            if (isTopMovie == false) {
                binding?.btnBuyTicket?.isEnabled = false
                binding?.btnBuyTicket?.setBackgroundResource(R.drawable.grey_bg)

            }


        }
        sharedVM.actors.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {

                    adapterActor.updatePersons(state.data)
                }

                is UIState.Loading -> {

                }

                is UIState.Error -> {

                    adapterActor.updatePersons(emptyList())
                }
            }
        }

        binding?.btBack?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.btnBuyTicket?.setOnClickListener {
            findNavController().navigate(R.id.action_filmDetailFragment_to_seatsFragment)
        }
    }

    private fun getBlurView() {

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






