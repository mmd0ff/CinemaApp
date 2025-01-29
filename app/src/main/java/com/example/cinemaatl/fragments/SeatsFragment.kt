package com.example.cinemaatl.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private  var binding: FragmentSeatsBinding? = null
    private val viewModel: MainVM by activityViewModels()

    private val adapterDate = MovieDateAdapter { position ->
        viewModel.selectDate(position)
        Log.d("SeatsFragment", "Date selected at position: $position")
    }

    private val adapterTime = MovieTimeAdapter { selectedTime ->
        viewModel.selectTime(selectedTime)
        Log.d("SeatsFragment", "Time selected: $selectedTime")
    }

 private lateinit var seatListAdapter: SeatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeatsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.dateRecyclerView?.adapter = adapterDate
        binding?.timeRecyclerView?.adapter = adapterTime

         seatListAdapter =
            SeatListAdapter(requireContext(), mutableListOf()) { selectedSeat, count ->
                binding?.tvCountPlace?.text = "Выбрано мест:$count "
//                binding.totalPrice.text = "Итого: ${count * SEAT_PRICE} $"
                val totalPrice = count * SEAT_PRICE
                binding?.totalPrice?.text = "Итого: $totalPrice $"

                viewModel.updateSelectedSeats(selectedSeat)
                viewModel.getTotalPrice(totalPrice)
            }
        binding?.rvSeats?.adapter = seatListAdapter



        viewModel.daysList.observe(viewLifecycleOwner) { days ->
            adapterDate.updateList(days)
        }

        viewModel.timeSlots.observe(viewLifecycleOwner) { time ->
            adapterTime.updateList(time)
        }

        initSeatList()

        binding?.buyTicket?.setOnClickListener {
            val selectedDate = viewModel.selectedDate.value
            val selectedTime = viewModel.selectedTime.value
            val selectedSeats = viewModel.selectedSeats.value
            val selectedMovie = viewModel.selectedMovie.value

            Log.d("SeatsFragment", "Selected Date: $selectedDate")
            Log.d("SeatsFragment", "Selected Time: $selectedTime")
            Log.d("SeatsFragment", "Selected Seats: $selectedSeats")
            Log.d("SeatsFragment", "Selected Seats: $selectedMovie")

                if (selectedDate != null && selectedTime != null && selectedSeats!!.isNotEmpty()) {
                    findNavController().navigate(R.id.action_seatsFragment_to_ticketFragment)

                } else{
                    Toast.makeText(
                        requireContext(),
                        "Please select date, time, and seats",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }
    private fun initSeatList() {

        val gridLayoutManager = GridLayoutManager(context,7)
        binding?.rvSeats?.layoutManager = gridLayoutManager

        val seatList = generateSeatList()
        seatListAdapter.updateSeatList(seatList)
        binding?.rvSeats?.isNestedScrollingEnabled = false

//            val gridLayoutManager = GridLayoutManager(context, 7)
//            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
////                return if(position %7 == 3)1 else 1
//                    return 1
//
//                }
//
//            }
//            val seatList = mutableListOf<Seat>()
//            val numberSeats = 60
//            val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M")
//            val seatPrice = 2
//
//            for (i in 0 until numberSeats) {
//                val row = rows[i / 5]
//                val seatNumber = (i % 5) + 1
//                val seatName = "$row$seatNumber"
////            val seatName = ""
//
//                // Определяем статус сидения (примеры занятых мест)
//                val SeatStatus =
//                    if (i == 2 || i == 20 || i == 21 || i == 33 || i == 41 || i == 42 || i == 50) Seat.SeatStatus.UNAVIABLE else Seat.SeatStatus.AVIABLE
//
//                seatList.add(Seat(SeatStatus, seatName))
//
//            }


//            val seatListAdapter =
//                SeatListAdapter(requireContext(), seatList) { selectedSeat, count ->
//                    binding.tvCountPlace.text = "Выбрано мест:$count "
//                    binding.totalPrice.text = "Итого: ${count * seatPrice} $"
//
//
//                    viewModel.updateSelectedSeats(selectedSeat)
//                }
//        val seatListAdapter = SeatListAdapter(requireContext(), seatList, object : SeatListAdapter.SelectedSeat {
//            override fun Return(selectedName: String, num: Int) {
//                binding.tvSelected.text = "Выбрано мест: $num"
//
//            }
//        })

//            binding.rvSeats.adapter = seatListAdapter
//
//            binding.rvSeats.isNestedScrollingEnabled = false


        }

    private fun generateSeatList(): List<Seat>{
        val numberSeats = 60
        var rows = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M")
        val unaviableSeats = listOf(2, 20, 21, 33, 41, 42, 50)

        return (0 until  numberSeats).map {i ->
            val row = rows[i / 5]
            val seatNumber = (i % 5) + 1
            val seatName = "$row$seatNumber"
            val seatStatus = if (unaviableSeats.contains(i)) Seat.SeatStatus.UNAVIABLE else Seat.SeatStatus.AVIABLE
            Seat(seatStatus, seatName)
        }
    }
    companion object {
        private const val SEAT_PRICE = 2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    }


