package com.example.cinemaatl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderTimeBinding

class MovieTimeAdapter(

    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MovieTimeAdapter.ViewHolderTime>() {

    private lateinit var context: Context
    private var timeSlots: List<String> = emptyList()

    private var selectedTime = -1
    private var lastSelectedTime = -1


    inner class ViewHolderTime(val binding: ViewholderTimeBinding) :
        ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieTimeAdapter.ViewHolderTime {
        context = parent.context
        val binding = ViewholderTimeBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderTime(binding)

    }

    override fun onBindViewHolder(holder: MovieTimeAdapter.ViewHolderTime, position: Int) {

        val timeSlot = timeSlots[position]

        holder.binding.tvTime.text = timeSlot

        holder.itemView.setOnClickListener {
            onItemClick(timeSlot)

        }
        holder.binding.tvTime.setOnClickListener {
            lastSelectedTime = selectedTime
            selectedTime = position
            notifyItemChanged(lastSelectedTime)
            notifyItemChanged(selectedTime)
        }

        if (selectedTime == position) {
            holder.binding.tvTime.setBackgroundResource(R.drawable.orange_bg)

        } else holder.binding.tvTime.setBackgroundResource(R.drawable.light_black_bg)

    }

    override fun getItemCount(): Int = timeSlots.size

    fun updateList(newItems: List<String>) {
        timeSlots = newItems
        notifyDataSetChanged()
    }

}