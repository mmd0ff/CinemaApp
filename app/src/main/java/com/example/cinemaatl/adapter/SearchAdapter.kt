package com.example.cinemaatl.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
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
    ): SearchAdapter.ViewHolderSearch {
        context = parent.context
        val binding = ViewholderSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolderSearch(binding)

    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolderSearch, position: Int) {
       val doc = movies[position]
        holder.binding.title.text = doc.name

        Glide.with(holder.binding.pic.context)
            .load(doc.poster.url)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(doc)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateData(newData: List<Doc>){
        movies.clear()
        movies.addAll(newData)
        notifyDataSetChanged()
    }


}