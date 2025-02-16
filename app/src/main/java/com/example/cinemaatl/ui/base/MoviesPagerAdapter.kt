package com.example.cinemaatl.ui.base

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.cinemaatl.ui.topmovie.TopMovieFragment
import com.example.cinemaatl.ui.upcomingmovies.UpcomingMovieFragment

class MoviesPagerAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int  = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TopMovieFragment()
            1-> UpcomingMovieFragment()
            else -> throw IllegalArgumentException("Invalid position")

        }
    }
}