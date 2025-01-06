package com.example.cinemaatl.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemaatl.R
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.MovieDateAdapter
import com.example.cinemaatl.adapter.MovieTimeAdapter
import com.example.cinemaatl.adapter.SeatListAdapter
import com.example.cinemaatl.databinding.FragmentSeatsBinding
import com.example.cinemaatl.model.Seat
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class SeatsFragment : Fragment() {

    private lateinit var binding: FragmentSeatsBinding

    private val viewModel: MainVM by activityViewModels()

    private val adapterDate = MovieDateAdapter{ position -> viewModel.selectDate(position)}
    private val adapterTime = MovieTimeAdapter{ selectedTime -> viewModel.selectTime(selectedTime)}



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeatsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.dateRecyclerView.adapter = adapterDate
        binding.timeRecyclerView.adapter = adapterTime




        viewModel.daysList.observe(viewLifecycleOwner){days ->
            adapterDate.updateList(days)
        }

        viewModel.timeSlots.observe(viewLifecycleOwner){time ->
            adapterTime.updateList(time)
        }




        initSeatList()

        binding.buyTicket.setOnClickListener {
            findNavController().navigate(R.id.action_seatsFragment_to_ticketFragment)
        }




    }

    private fun initSeatList() {
        val gridLayoutManager = GridLayoutManager(context, 7)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
//                return if(position %7 == 3)1 else 1
                return 1

            }

        }
        val seatList = mutableListOf<Seat>()
        val numberSeats = 60
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M")
        val seatPrice = 2

        for (i in 0 until numberSeats) {
            val row = rows[i / 5]
            val seatNumber = (i % 5) + 1
            val seatName = "$row$seatNumber"
//            val seatName = ""


            // Определяем статус сидения (примеры занятых мест)
            val SeatStatus =
                if (i == 2 || i == 20 || i == 21 || i == 33 || i == 41 || i == 42 || i == 50 ) Seat.SeatStatus.UNAVIABLE else Seat.SeatStatus.AVIABLE

            seatList.add(Seat(SeatStatus, seatName))

        }

        val seatListAdapter = SeatListAdapter(requireContext(), seatList) { selectedNames, count ->
            binding.tvCountPlace.text = "Выбрано мест:$count "
            binding.totalPrice.text = "Итого: ${count * seatPrice} $"
        }
//        val seatListAdapter = SeatListAdapter(requireContext(), seatList, object : SeatListAdapter.SelectedSeat {
//            override fun Return(selectedName: String, num: Int) {
//                binding.tvSelected.text = "Выбрано мест: $num"
//
//            }
//        })


        binding.rvSeats.adapter = seatListAdapter
        binding.rvSeats.isNestedScrollingEnabled = false


    }





}
