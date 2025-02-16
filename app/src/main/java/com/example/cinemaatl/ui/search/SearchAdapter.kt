package com.example.cinemaatl.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderSearchBinding
import com.example.cinemaatl.model.Doc

class SearchAdapter():RecyclerView.Adapter<SearchAdapter.ViewHolderSearch>() {
    private lateinit var context: Context
    private val movies = mutableListOf<Doc>()
    var itemClickListener: ((Doc) -> Unit)? = null

   inner class ViewHolderSearch(val binding: ViewholderSearchBinding):
   ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderSearch {
        context = parent.context
        val binding = ViewholderSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderSearch(binding)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolderSearch, position: Int) {
       val movies = movies[position]
        holder.binding.title.text = movies.name

        val poster = movies.poster
        if( poster != null)
        Glide.with(holder.binding.pic.context)
            .load(movies.poster.url)
            .error(R.drawable.ic_noimage)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(movies)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateData(newData: List<Doc>){
        movies.clear()
        movies.addAll(newData)
        notifyDataSetChanged()
    }


}