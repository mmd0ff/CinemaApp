package com.example.cinemaatl.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.databinding.ViewholderUserticketsBinding
import com.example.cinemaatl.model.Doc
import com.example.cinemaatl.model.TicketModel

class UserTicketsAdapter( initialMovies: List<Doc>): RecyclerView.Adapter<UserTicketsAdapter.ViewHolderUserTickets>() {

    private lateinit var context: Context
    private val dataList = mutableListOf<TicketModel>()
    private var movies = initialMovies.toMutableList()



        inner  class ViewHolderUserTickets(val binding: ViewholderUserticketsBinding):
        ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserTicketsAdapter.ViewHolderUserTickets {
        context = parent.context
        val binding = ViewholderUserticketsBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderUserTickets(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserTicketsAdapter.ViewHolderUserTickets, position: Int) {
        val ticket = dataList[position]
        val movie = movies.find { it.name == ticket.movieName }
        if (movie != null) {
            holder.binding.seats.text = ticket.movieName
            holder.binding.seats.text = "Row: ${ticket.selectedSeats.joinToString(",")}"
            holder.binding.date.text = "Date: ${ticket.selectedDate}"
            holder.binding.time.text = "Time: ${ticket.selectedTime}"
            holder.binding.price.text = "Price: ${ticket.totalPrice}"
            holder.binding.movieTitle.text = ticket.movieName


            Glide.with(holder.binding.moviePoster.context)
                .load(movie.poster.url)
                .into(holder.binding.moviePoster)


        } else {
            holder.binding.movieTitle.text = "Фильм не найден"

        }
    }
    override fun getItemCount(): Int = dataList.size

    fun updateMovies(newMovies: List<Doc>){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }


    fun updateList(newList: List<TicketModel>){
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }

}