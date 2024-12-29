package com.example.cinemaatl.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.ApiResult
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.UIState
import com.example.cinemaatl.apiCall
import com.example.cinemaatl.model.Doc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject


@HiltViewModel
class MainVM @Inject constructor(
    private val movieService: MovieService // Интерфейс для запросов
) : ViewModel() {

    private val _currentPage = MutableLiveData(0)
    val currentPage:LiveData<Int> = _currentPage

    private var autoScrollJob: Job? = null
    private val timer: Timer? = null

    private val _movies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val movies: LiveData<UIState<List<Doc>>>  = _movies

    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
    val selectedMovie: LiveData<Doc?> = _selectedMovie

    fun getTopMovies(page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            _movies.value = UIState.Loading(true) // Установка состояния загрузки

            val result = apiCall {
                movieService.get250TopMovies(
                    page = page,
                    limit = limit,
                    lists = "top250",
                    sortField = "top250",
                    sortType = "1",
//                    selectFields = "id,name,description"
                )
            }

            when (result) {
                is ApiResult.Success -> {
                    val movieList = result.data?.docs // Получаем список фильмов
                    if (movieList.isNullOrEmpty()) {
                        _movies.value = UIState.Error(null, "Список фильмов пуст")
                    } else {
                        _movies.value = UIState.Success(movieList)
                    }
                }
                is ApiResult.Error -> {
                    _movies.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage
                    )
                }
            }
        }
    }

    fun getUpcomingMovies(page: Int =1, limit: Int = 10) {
        viewModelScope.launch {
            _movies.value = UIState.Loading(true) // Установка состояния загрузки

            val result = apiCall {
                movieService.getUpcomingMovies(
                    page =page,
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

    fun selectedMovie(movie: Doc){
        _selectedMovie.value = movie
    }
}

