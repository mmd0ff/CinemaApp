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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchVM
@Inject
constructor(

    private val movieService: MovieService,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth// Интерфейс для запросов
) : ViewModel() {


    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
        MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

    private val _selectedMovie: MutableLiveData<UIState<List<Doc?>>> = MutableLiveData()
    val selectedMovie: LiveData<UIState<List<Doc?>>> = _selectedMovie


    private val _searchResult: MutableLiveData<UIState<List<Doc>>> = MutableLiveData()
    val searchResult: LiveData<UIState<List<Doc>>> = _searchResult

    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String> = _searchQuery


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }





    fun getMovieId(id:String){
        viewModelScope.launch {
            _selectedMovie.value = UIState.Loading(true)

            val result = apiCall {
                movieService.getActorByID(id)
            }
            when(result){
                is ApiResult.Success -> {
                    val movieId = result.data?.docs
                    if (movieId.isNullOrEmpty()){
                        _selectedMovie.value = UIState.Error(null, "Movie Not Found")
                    } else {
                        _selectedMovie.value = UIState.Success(movieId)
                    }
                }
                is ApiResult.Error -> {
                    _selectedMovie.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage
                    )
                }
            }
        }
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


//    fun selectedMovie(movie: Doc) {
//        Log.d("MainVM", "Selected movie: ${movie.name}")
//        Log.d("MainVM", "Persons list: ${movie.persons}") // Логируем список актеров
//        _selectedMovie.value = movie
//
//    }


}




