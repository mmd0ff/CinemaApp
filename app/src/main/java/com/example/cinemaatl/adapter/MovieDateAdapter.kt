package com.example.cinemaatl.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderDateBinding
import com.example.cinemaatl.model.DateModel

class MovieDateAdapter(
    private val onDateClick: (Int) -> Unit
) : RecyclerView.Adapter<MovieDateAdapter.ViewHolderDate>() {
    private lateinit var context: Context
    private val dateList = mutableListOf<DateModel>()

    private var selectedPosition = -1
    private var lastSelectPosition = -1

    inner class ViewHolderDate(val binding: ViewholderDateBinding) :
        ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDateAdapter.ViewHolderDate {
        context = parent.context
        val binding = ViewholderDateBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderDate(binding)

    }


    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: MovieDateAdapter.ViewHolderDate,
        @SuppressLint("RecyclerView") position: Int
    ) {

        val date = dateList[position]

        holder.binding.tvDayOfWeek.text = date.dayOfWeek
        holder.binding.tvDayOfMonth.text = date.dayOfMonth.toString()

        holder.binding.root.setOnClickListener {
            onDateClick(position)


        }
        holder.binding.dateBlock.setOnClickListener {
            lastSelectPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(selectedPosition)
            notifyItemChanged(lastSelectPosition)

        }
        if (selectedPosition == position) {
            holder.binding.dateBlock.setBackgroundResource(R.drawable.orange_bg)
        } else holder.binding.dateBlock.setBackgroundResource(R.drawable.light_black_bg)

    }

    override fun getItemCount(): Int = dateList.size


    fun updateList(newItems: List<DateModel>) {
        dateList.clear()
        dateList.addAll(newItems)
        notifyDataSetChanged()
    }


}