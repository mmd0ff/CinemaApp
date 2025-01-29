package com.example.cinemaatl.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.GenreAdapter
import com.example.cinemaatl.databinding.FragmentFilmDetailBinding
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class FilmDetailFragment : Fragment() {
    private var binding: FragmentFilmDetailBinding? = null
    private val viewModel: MainVM by activityViewModels()

    private val adapterGenre = GenreAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFilmDetailBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.rvGenre?.adapter = adapterGenre

        getBlurView()



        viewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->
            binding?.tvDesc?.text = movie?.description.toString()
            binding?.movieTitle?.text = movie?.name.toString()
            binding?.tvYear?.text = movie?.year.toString()
            binding?.duration?.text = movie?.movieLength.toString()
            binding?.rating?.text = movie?.rating.toString()

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

        }

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