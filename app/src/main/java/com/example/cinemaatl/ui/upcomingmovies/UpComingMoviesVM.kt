package com.example.cinemaatl.ui.upcomingmovies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.ApiResult
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.UIState
import com.example.cinemaatl.helper.apiCall
import com.example.cinemaatl.model.Doc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpComingMoviesVM
@Inject constructor(
    private val movieService: MovieService,

) : ViewModel() {


    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
        MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

//    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
//    val selectedMovie: LiveData<Doc?> = _selectedMovie

    private val _movies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val movies: LiveData<UIState<List<Doc>>> = _movies

    private val _buttonState = MutableLiveData<Boolean>()
    val buttonState:LiveData<Boolean> = _buttonState


    fun setButtonState(isTopMovie: Boolean) {
        _buttonState.value = isTopMovie
    }


//    fun getMovieId(id: String) {
//        viewModelScope.launch {
//            _selectedMovie.value = null
//            _movies.value = UIState.Loading(true)
//
//            val result = apiCall {
//                movieService.getActorByID(id)
//            }
//            when (result) {
//                is ApiResult.Success -> {
//
//                    val movieId = result.data?.docs?.firstOrNull()
//                    if (movieId != null) {
//                        _selectedMovie.value = movieId
//                        _movies.value = UIState.Success(listOf(movieId))
//
//                    } else {
//                        _movies.value = UIState.Error(null, "Movie Not Found")
//
//                    }
//                }
//
//                is ApiResult.Error -> {
//                    _movies.value = UIState.Error(
//                        result.error?.errorCode,
//                        result.error?.errorMessage
//                    )
//                }
//            }
//        }
//    }


    fun getUpcomingMovies(page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            _movies.value = UIState.Loading(true) // Установка состояния загрузки

            val result = apiCall {
                movieService.getUpcomingMovies(
                    page = page,
                    limit = limit,
                    lists = "planned-to-watch-films",
                )
            }

            when (result) {
                is ApiResult.Success -> {
                    val movieList = result.data?.docs // Получаем список фильмов
                    if (movieList.isNullOrEmpty()) {
                        _upcomingMovies.value = UIState.Error(null, "Список фильмов пуст")
                    } else {
                        _upcomingMovies.value = UIState.Success(movieList)
                    }
                }

                is ApiResult.Error -> {
                    _upcomingMovies.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage
                    )
                }
            }
        }
    }

//
//    fun selectedMovie(movie: Doc) {
//        Log.d("MainVM", "Selected movie: ${movie.name}")
//        Log.d("MainVM", "Persons list: ${movie.persons}") // Логируем список актеров
//        _selectedMovie.value = movie
//
//    }


}