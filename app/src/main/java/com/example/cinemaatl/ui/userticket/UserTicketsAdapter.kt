package com.example.cinemaatl.ui.userticket

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat.performHapticFeedback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderUserticketsBinding
import com.example.cinemaatl.model.TicketModel

class UserTicketsAdapter(

    private val onDeleteClickListener: (TicketModel) -> Unit
) : RecyclerView.Adapter<UserTicketsAdapter.ViewHolderUserTickets>() {

    private lateinit var context: Context
    private val dataList = mutableListOf<TicketModel>()


    inner class ViewHolderUserTickets(val binding: ViewholderUserticketsBinding) :
        ViewHolder(binding.root)
//            init {
//                binding.root.setOnLongClickListener{
//                    val position = adapterPosition
//                    if(position!= RecyclerView.NO_POSITION){
//                        val ticket = dataList[position]
//                        onDeleteClickListener(ticket)
//                    }
//                    true
//                }
//            }
//        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderUserTickets {
        context = parent.context
        val binding =
            ViewholderUserticketsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderUserTickets(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderUserTickets, position: Int) {
        val ticket = dataList[position]
//        val movie = movies.find { it.name == ticket.movieName }


        holder.binding.seats.text = "Row: ${ticket.selectedSeats.joinToString(",")}"
        holder.binding.date.text = "Date: ${ticket.selectedDate}"
        holder.binding.time.text = "Time: ${ticket.selectedTime}"
        holder.binding.price.text = "Price: ${ticket.totalPrice}$"
        holder.binding.movieTitle.text =
            ticket.movieName.takeIf { it.isNotEmpty() } ?: "Фильм не найден"

        if (ticket.moviePosterUrl.isNotEmpty()) {
            Glide.with(holder.binding.moviePoster.context)
                .load(ticket.moviePosterUrl)
                .into(holder.binding.moviePoster)
        } else {
            Log.w("UserTicketsAdapter", "No poster URL for ${ticket.movieName}")
            holder.binding.moviePoster.setImageResource(R.drawable.ic_noimage)
        }

        holder.binding.root.setOnLongClickListener {
            onDeleteClickListener(ticket)
            it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            true
        }
    }
//        holder.binding.root.setOnLongClickListener {
//            val pos = holder.adapterPosition
//            if (pos != RecyclerView.NO_POSITION) {
//                val ticketAtPos = dataList[pos]
//                Log.d("UserTicketsAdapter", "Long press on ticket: ${ticketAtPos.movieName}")
//                it.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
//                onDeleteClickListener(ticketAtPos)
//            }
//            true
//        }
//    }

    override fun getItemCount(): Int = dataList.size


    fun updateList(newList: List<TicketModel>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }

}