package com.example.cinemaatl.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.common.ApiResult
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.ui.core.UIState
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


    private val _searchResult: MutableLiveData<UIState<List<Doc>>> = MutableLiveData()
    val searchResult: LiveData<UIState<List<Doc>>> = _searchResult



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




