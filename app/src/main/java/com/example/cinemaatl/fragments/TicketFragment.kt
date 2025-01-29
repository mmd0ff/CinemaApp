package com.example.cinemaatl.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.databinding.FragmentTicketBinding
import com.example.cinemaatl.model.TicketModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.times


@AndroidEntryPoint
class TicketFragment : Fragment() {

    private  var binding: FragmentTicketBinding? = null
    private val viewModel: MainVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.selectedMovie.observe(viewLifecycleOwner) { movie ->
            binding?.posterImageView?.let { imageView ->
                movie?.poster?.url?.let { url ->
                    Glide.with(imageView.context)
                        .load(url)
                        .into(imageView)
                }
            }
        }

        //наблюдаем за выбранной датой
        viewModel.selectedDate.observe(viewLifecycleOwner){selectedDate ->
            Log.d("TicketFragment", "Observed Selected Date: $selectedDate")
//            binding.tvDate.text = selectedClickDate.toString()
            if(selectedDate != null){
                binding?.tvDate?.text = "${selectedDate.dayOfMonth} ${selectedDate.dayOfWeek}"
            }
        }
        viewModel.selectedTime.observe(viewLifecycleOwner){selectedTime ->
            Log.d("TicketFragment", "Observed Selected Time: $selectedTime")
            if ( selectedTime != null){
                binding?.tvTime?.text = "$selectedTime"
            }
        }
        viewModel.totalPrices.observe(viewLifecycleOwner){totalPrice ->
            Log.d("TicketFragment", "Observed Total Price: $totalPrice")
            if (totalPrice != null){
                binding?.totalPrice?.text = "$totalPrice $"
            }
        }

        viewModel.selectedSeats.observe(viewLifecycleOwner){selectedSeats ->
            Log.d("SeatsFragment", "Observed Selected Seats: $selectedSeats")

            if (!selectedSeats.isNullOrEmpty()){
                binding?.tvSeats?.text = "Row: ${selectedSeats.joinToString(",")}"
//                binding.totalPrice.text = "Total price ${selectedSeats.size}"
            }

        }

        binding?.btReturn?.setOnClickListener {
            findNavController().navigate(R.id.baseFragment)
        }
//        binding?.?.setOnClickListener {
//            val ticket = TicketModel(
//                movieId = viewModel.selectedMovie.value?.id ?: "Unknown Movie",
//
//                movieName = viewModel.selectedMovie.value?.name ?: "",
//
//                moviePosterUrl = viewModel.selectedMovie.value?.poster?.url ?: "",
//
//                selectedSeats = viewModel.selectedSeats.value?: emptyList(),
//
//                selectedDate = viewModel.selectedDate.value?.let { date ->
//                    "${date.dayOfMonth}"
//                } ?:"",
//                selectedTime = viewModel.selectedTime.value ?: "",
//
//                totalPrice = viewModel.totalPrices.value ?: 0
//
//            )
//            viewModel.saveTicket(ticket)
//
//            findNavController().navigate(R.id.userTicketsFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}