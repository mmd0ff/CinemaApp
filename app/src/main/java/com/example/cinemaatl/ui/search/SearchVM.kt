package com.example.cinemaatl.ui.search

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
class SearchVM
@Inject
constructor(

    private val movieService: MovieService,

    ) : ViewModel() {


    private val _selectedMovie: MutableLiveData<UIState<List<Doc?>>> = MutableLiveData()
    val selectedMovie: LiveData<UIState<List<Doc?>>> = _selectedMovie


    private val _searchResult: MutableLiveData<UIState<List<Doc>>> = MutableLiveData()
    val searchResult: LiveData<UIState<List<Doc>>> = _searchResult

    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String> = _searchQuery


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }


    fun searchAllMovies(query: String) {
        viewModelScope.launch {
            _searchResult.value = UIState.Loading(true)

            val result = apiCall {
                movieService.searchMovies(query = query)
            }
            when (result) {
                is ApiResult.Success -> {
                    val movieList = result.data?.docs
                    if (movieList.isNullOrEmpty()) {
                        _searchResult.value = UIState.Error(null, "Movie not Founded")
                    } else {

                        _searchResult.value = UIState.Success(movieList)
                    }
                }

                is ApiResult.Error -> {
                    _searchResult.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage
                    )
                }
            }
        }
    }


}




