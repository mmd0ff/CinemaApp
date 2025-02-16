package com.example.cinemaatl.ui.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.ui.base.MainVM
import com.example.cinemaatl.databinding.FragmentTicketBinding
import com.example.cinemaatl.ui.base.SharedVM
import com.example.cinemaatl.ui.userticket.CompletePaymentDialogFragment
import com.example.cinemaatl.ui.seats.SeatsVM
import com.example.cinemaatl.ui.topmovie.TopMovieVM
import com.example.cinemaatl.ui.userticket.UserTicketVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TicketFragment : Fragment() {

    private  var binding: FragmentTicketBinding? = null
//    private val viewModel: MainVM by activityViewModels()
//    private val viewModelTop by viewModels<TopMovieVM>()
//    private val viewModelSeats by viewModels<SeatsVM>()
    private val sharedVM by activityViewModels<SharedVM>()
//   private val viewModelTop by activityViewModels<TopMovieVM>()
     private val viewModelSeats by activityViewModels<SeatsVM>()
    private val viewModelUserTickets by activityViewModels<UserTicketVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedVM.selectedMovie.observe(viewLifecycleOwner) { movie ->
            binding?.posterImageView?.let { imageView ->
                movie?.poster?.url?.let { url ->
                    Glide.with(imageView.context)
                        .load(url)
                        .into(imageView)
                }
            }
        }

        //наблюдаем за выбранной датой
        viewModelSeats.selectedDate.observe(viewLifecycleOwner) { selectedDate ->
            Log.d("TicketFragment", "Observed Selected Date: $selectedDate")
//            binding.tvDate.text = selectedClickDate.toString()
            if (selectedDate != null) {
                binding?.tvDate?.text = "${selectedDate.dayOfMonth} ${selectedDate.dayOfWeek}"
            }
        }

        viewModelSeats.selectedTime.observe(viewLifecycleOwner) { selectedTime ->
            Log.d("TicketFragment", "Observed Selected Time: $selectedTime")
            if (selectedTime != null) {
                binding?.tvTime?.text = "$selectedTime"
            }
        }

        viewModelSeats.totalPrices.observe(viewLifecycleOwner) { totalPrice ->
            Log.d("TicketFragment", "Observed Total Price: $totalPrice")
            if (totalPrice != null) {
                binding?.totalPrice?.text = "$totalPrice $"
            }
        }


        viewModelSeats.selectedSeats.observe(viewLifecycleOwner) { selectedSeats ->
            Log.d("SeatsFragment", "Observed Selected Seats: $selectedSeats")

            if (!selectedSeats.isNullOrEmpty()) {
                binding?.tvSeats?.text =
                    getString(R.string.row_inticket, selectedSeats.joinToString(","))
                binding?.totalPrice?.text = "Total price ${selectedSeats.size}"
            }

        }
        startTimer()

        val ticket = viewModelUserTickets.createTicket()
        viewModelUserTickets.saveTicket(ticket)

//        binding?.btReturn?.setOnClickListener {
//            findNavController().navigate(R.id.baseFragment)
//        }



    }

    private fun startTimer(){
        val timer = object : CountDownTimer(10000,1000) {
            @SuppressLint("StringFormatMatches")
            override fun onTick(millisUntilFinished: Long) {
                binding?.timer?.text =
                    getString(R.string.payment_finish_after_seconds, millisUntilFinished / 1000)
            }

            override fun onFinish() {
                val dialog = CompletePaymentDialogFragment()
                dialog.show(parentFragmentManager, "CompletePaymentDialogFragment")


            }

        }
        timer.start()
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



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

