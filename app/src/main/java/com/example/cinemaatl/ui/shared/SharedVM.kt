package com.example.cinemaatl.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.common.ApiResult
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.helper.apiCall
import com.example.cinemaatl.model.Doc
import com.example.cinemaatl.model.Person
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedVM @Inject constructor(
    private val movieService: MovieService
) : ViewModel() {

    private val _selectedMovie = MutableLiveData<Doc>()
    val selectedMovie: LiveData<Doc> = _selectedMovie

    private val _isMovieTop = MutableLiveData<Boolean>()
    val isMovieTop: LiveData<Boolean> = _isMovieTop


    private val _actors: MutableLiveData<UIState<List<Person>>> = MutableLiveData()
    val actors: LiveData<UIState<List<Person>>> = _actors


    fun selectedMovie(movie: Doc, isMovieTop: Boolean) {
        _selectedMovie.value = movie
        _isMovieTop.value = isMovieTop
        if (movie != null) {
            getActors4Movie(movie.id ?: "")
        }

    }

    private fun getActors4Movie(movieId: String) {
        viewModelScope.launch {
            Log.d("SharedVM", "Starting to load actors for movieId: $movieId")
            _actors.value = UIState.Loading(true)
            val result = apiCall { movieService.getActorByID(movieId) }
            when (result) {
                is ApiResult.Success -> {
                    val actorsList = result.data?.persons ?: emptyList()
                    Log.d("SharedVM", "Actors loaded: count=${actorsList.size}")
                    if (actorsList.isNotEmpty()) {
                        Log.d(
                            "SharedVM",
                            "First actor: name=${actorsList.firstOrNull()?.name}, photo=${actorsList.firstOrNull()?.photo}"
                        )
                    }
                    _actors.value = if (actorsList.isEmpty()) {
                        UIState.Error(null, "Актёры не найдены")
                    } else {
                        UIState.Success(actorsList)
                    }
                }

                is ApiResult.Error -> {
                    Log.e(
                        "SharedVM",
                        "Error loading actors: code=${result.error?.errorCode}, message=${result.error?.errorMessage}"
                    )
                    _actors.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage ?: "Ошибка загрузки актёров"
                    )
                }
            }
        }
    }

}