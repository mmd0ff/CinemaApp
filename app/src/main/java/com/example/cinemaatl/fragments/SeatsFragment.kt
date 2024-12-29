package com.example.cinemaatl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemaatl.adapter.SeatListAdapter
import com.example.cinemaatl.databinding.FragmentSeatsBinding
import com.example.cinemaatl.model.Seat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeatsFragment : Fragment() {
    private lateinit var binding: FragmentSeatsBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeatsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initSeatList()




    }
    private fun initSeatList(){
        val gridLayoutManager = GridLayoutManager(context,7)
        gridLayoutManager.spanSizeLookup = object :GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
//                return if(position %7 == 3)1 else 1
                return 1

            }

        }
        val seatList = mutableListOf<Seat>()
        val numberSeats = 81
        val rows = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I","K","L","M")
        val seatPrice = 2

        for(i in 0 until numberSeats){
            val row = rows[i / 7]
            val seatNumber = (i % 7) + 1
            val seatName = "$row$seatNumber"
//            val seatName = ""


            // Определяем статус сидения (примеры занятых мест)
            val SeatStatus = if (i ==2  || i==20 || i==21 || i==33 || i==41 || i==42 || i==50|| i==72 || i==73|| i==73 ) Seat.SeatStatus.UNAVIABLE else Seat.SeatStatus.AVIABLE

            seatList.add(Seat(SeatStatus,seatName))

        }

        val seatListAdapter = SeatListAdapter(requireContext(), seatList){selectedNames, count ->
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
