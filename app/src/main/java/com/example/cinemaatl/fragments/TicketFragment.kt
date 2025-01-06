package com.example.cinemaatl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.databinding.FragmentTicketBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TicketFragment : Fragment() {

    private lateinit var binding: FragmentTicketBinding
    private val viewModel: MainVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->

            Glide.with(binding.posterImageView.context)
                .load(movie?.poster?.url)
                .into(binding.posterImageView)


        }

        viewModel.selectedDate.observe(viewLifecycleOwner){selectedDate ->
            selectedDate?.let {
                binding.tvDate.text = it.toString()
            }
        }




    }

}