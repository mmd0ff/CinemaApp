package com.example.cinemaatl.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.model.Doc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SharedVM @Inject constructor() : ViewModel() {

    private val _selectedMovie = MutableLiveData<Doc>()
    val selectedMovie: LiveData<Doc> = _selectedMovie

     fun selectedMovie(movie:Doc){
         _selectedMovie.value = movie

     }
}