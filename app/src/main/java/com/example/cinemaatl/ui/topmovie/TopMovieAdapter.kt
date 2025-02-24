package com.example.cinemaatl.ui.topmovie

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.databinding.ViewholderTopBinding
import com.example.cinemaatl.model.Doc

class TopMovieAdapter() : RecyclerView.Adapter<TopMovieAdapter.ViewHolderTop>() {

    private lateinit var context: Context
    private val movies = mutableListOf<Doc>()
    var itemClickListener: ((Doc) -> Unit)? = null


    inner class ViewHolderTop(val binding: ViewholderTopBinding) :
        ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTop {
        context = parent.context
        val binding = ViewholderTopBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderTop(binding)


    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolderTop, position: Int) {
        val doc = movies[position]
        holder.binding.title.text = doc.name


        Glide.with(holder.binding.pic.context)
            .load(doc.poster?.url)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(doc)

        }
    }

    fun updateData(newData: List<Doc>) {
        movies.clear()
        movies.addAll(newData)
        notifyDataSetChanged()
    }
}