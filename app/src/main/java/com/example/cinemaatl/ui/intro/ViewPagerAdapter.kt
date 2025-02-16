package com.example.cinemaatl.ui.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaatl.databinding.ViewholderPagerBinding

class ViewPagerAdapter(
    private val images: List<Int>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPager>(){

      inner class ViewHolderPager(private val binding: ViewholderPagerBinding):
      RecyclerView.ViewHolder(binding.root){
          fun bind(imageRes:Int){
              binding.introImage.setImageResource(imageRes)
          }

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPager {
      val binding = ViewholderPagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolderPager(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolderPager, position: Int) {
        holder.bind(images[position])
    }


}
