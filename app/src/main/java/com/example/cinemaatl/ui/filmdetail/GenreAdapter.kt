package com.example.cinemaatl.ui.filmdetail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cinemaatl.databinding.ViewholderGenreBinding
import com.example.cinemaatl.model.Genres

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.ViewHolderGenre>() {

    private lateinit var context: Context

    private val genreList = mutableListOf<Genres>()

    inner class ViewHolderGenre(val binding: ViewholderGenreBinding) :
        ViewHolder(binding.root)


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderGenre {
        context = parent.context
        val binding = ViewholderGenreBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderGenre(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderGenre, position: Int) {

        val genres = genreList[position]

        holder.binding.tvGenre.text = genres.name


    }

    override fun getItemCount(): Int = genreList.size

    fun updateGenre(newGenreList: List<Genres>) {
        genreList.clear()
        genreList.addAll(newGenreList)
        notifyDataSetChanged()

    }

}