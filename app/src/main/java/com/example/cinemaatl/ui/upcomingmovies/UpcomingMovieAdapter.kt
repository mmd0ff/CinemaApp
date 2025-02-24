package com.example.cinemaatl.ui.upcomingmovies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.databinding.ViewholderTopBinding
import com.example.cinemaatl.model.Doc

class UpcomingMovieAdapter:RecyclerView.Adapter<UpcomingMovieAdapter.ViewHolderTop>() {

    private lateinit var context: Context
    private val dataList = mutableListOf<Doc>()
    var buttonChangeListener: (Boolean)->Unit = {}

     var itemClickListener: ((Doc) -> Unit)? = null


    inner class ViewHolderTop(val binding: ViewholderTopBinding):
    ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTop {
        context = parent.context
        val binding = ViewholderTopBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderTop(binding)


    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolderTop, position: Int) {
        val movies = dataList[position]
        holder.binding.title.text = movies.name

        val poster = movies.poster

        if(poster != null){


        Glide.with(holder.binding.pic.context)
            .load(movies.poster.url)
            .into(holder.binding.pic)
    } else {


        }
        holder.itemView.setOnClickListener {
            buttonChangeListener(false)

            itemClickListener?.invoke(movies)

        }
    }

    fun updateData(newData: List<Doc>){
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()

    }
}