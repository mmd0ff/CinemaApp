package com.example.cinemaatl.ui.seats

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderSeatBinding
import com.example.cinemaatl.model.Seat

class SeatListAdapter(
    private var context: Context,
    private val seatList: MutableList<Seat>,
//    private val selectedSeat: SelectedSeat
    private val onSeatSelected: (List<String>, Int) -> Unit
) : RecyclerView.Adapter<SeatListAdapter.ViewHolderSeat>() {

    private val selectedSeatName = ArrayList<String>()


    inner class ViewHolderSeat(val binding: ViewholderSeatBinding) :
        ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderSeat {
        context = parent.context
        val binding = ViewholderSeatBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderSeat(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderSeat, position: Int) {
        val seat = seatList[position]
//        holder.binding.seat.text = seat.name

        when (seat.status) {
            Seat.SeatStatus.AVIABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.seat_aviable)

            }

            Seat.SeatStatus.UNAVIABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.seat_unaviable)

            }

            Seat.SeatStatus.SELECTED -> {
                holder.binding.seat.setBackgroundResource(R.drawable.seat_selected)

            }

        }
        holder.binding.seat.setOnClickListener {
            when (seat.status) {
                Seat.SeatStatus.AVIABLE -> {
                    seat.status = Seat.SeatStatus.SELECTED
                    selectedSeatName.add(seat.name)
                    notifyItemChanged(position)
                }

                Seat.SeatStatus.SELECTED -> {
                    seat.status = Seat.SeatStatus.AVIABLE
                    selectedSeatName.remove(seat.name)
                    notifyItemChanged(position)
                }

                else -> {
                    
                }

            }

            onSeatSelected(selectedSeatName, selectedSeatName.size)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = seatList.size

    fun updateSeatList(newSeatList: List<Seat>) {
        seatList.clear()
        seatList.addAll(newSeatList)
        notifyDataSetChanged()


    }

}