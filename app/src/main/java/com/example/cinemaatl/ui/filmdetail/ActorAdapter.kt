package com.example.cinemaatl.ui.filmdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.ViewholderActorBinding
import com.example.cinemaatl.model.Person

class ActorAdapter:RecyclerView.Adapter<ActorAdapter.ViewHolderActor>() {
    private lateinit var context: Context
    private val actorList = mutableListOf<Person>()


    inner class ViewHolderActor(val binding: ViewholderActorBinding):
    ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderActor {
        context = parent.context
        val binding = ViewholderActorBinding.inflate(LayoutInflater.from(context), parent, false)
        return  ViewHolderActor(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderActor, position: Int) {

        val person = actorList[position]
        holder.binding.actorName.text = person.name

        val photos = person.photo

        if(!photos.isNullOrEmpty()){

            Glide.with(holder.binding.imageActor.context)
                .load(person.photo)
                .error(R.drawable.ic_noimage)
                .into(holder.binding.imageActor)
        } else {

        }


    }
    override fun getItemCount(): Int = actorList.size


    fun updatePersons(newPersonList: List<Person>?){
        actorList.clear()
        if (newPersonList != null) {
            actorList.addAll(newPersonList)
        }
        notifyDataSetChanged()
    }

}