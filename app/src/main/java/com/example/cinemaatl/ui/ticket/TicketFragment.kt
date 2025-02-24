package com.example.cinemaatl.ui.ticket

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.cinemaatl.BookingVM
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentTicketBinding
import com.example.cinemaatl.ui.base.SharedVM
import com.example.cinemaatl.ui.userticket.CompletePaymentDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class TicketFragment : Fragment() {

    private var binding: FragmentTicketBinding? = null

    private val sharedVM by activityViewModels<SharedVM>()

//    private val viewModelSeats by activityViewModels<SeatsVM>()
//    private val viewModelUserTickets by activityViewModels<UserTicketVM>()
    private val bookingVM by activityViewModels<BookingVM>()
    private val ticketVM by activityViewModels<TicketVM>()
    private var timer:CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTicketBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }



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

        bookingVM.createdTicket.observe(viewLifecycleOwner) { ticket ->
            if (ticket != null) {
                binding?.tvDate?.text = ticket.selectedDate // Только день, как было в createTicket
                binding?.tvTime?.text = ticket.selectedTime
                binding?.tvSeats?.text =
                    getString(R.string.row_inticket, ticket.selectedSeats.joinToString(", "))
                binding?.totalPrice?.text = "${ticket.totalPrice} $"

                // Запускаем таймер
                startTimer()
            }
        }
    }
    private fun startTimer() {
        timer?.cancel() // Отменяем предыдущий таймер, если был
        timer = object : CountDownTimer(1000, 1000) { // 10 секунд
            override fun onTick(millisUntilFinished: Long) {
                if (binding != null) {
                    binding!!.timer.text = getString(R.string.payment_finish_after_seconds, millisUntilFinished / 1000)
                }
            }

            override fun onFinish() {
                val ticket = bookingVM.createdTicket.value
                if(ticket != null){

                    Log.d("TicketFragment", "Saving ticket: id=${ticket.id}, movieName=${ticket.movieName}")
                    ticketVM.saveTicket(ticket)
                }

                val dialog = CompletePaymentDialogFragment()
                dialog.show(parentFragmentManager, "CompletePaymentDialogFragment")
            }
        }
        timer!!.start()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel() // Останавливаем таймер при уничтожении фрагмента
        binding = null
    }
}


