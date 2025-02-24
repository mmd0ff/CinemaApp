package com.example.cinemaatl.ui.topmovie

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
class TopMovieVM @Inject constructor(
    private val movieService: MovieService
) : ViewModel() {


    private val _movies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val movies: LiveData<UIState<List<Doc>>> = _movies


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
                    val movieList = result.data?.docs ?: emptyList() // Получаем список фильмов
                    if (movieList.isEmpty()) {
                        _movies.value = UIState.Error(null, "Список фильмов пуст")
                    } else {
                        movieList.forEach{it.isTopMovie = true}
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

}


