package com.example.cinemaatl.ViewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.ApiResult
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.UIState
import com.example.cinemaatl.apiCall
import com.example.cinemaatl.model.DateModel
import com.example.cinemaatl.model.Doc
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.Timer
import javax.inject.Inject



@HiltViewModel
class MainVM @Inject constructor(
    private val movieService: MovieService // Интерфейс для запросов
) : ViewModel() {

    private val _daysList: MutableLiveData<List<DateModel>> = MutableLiveData()
    val daysList: LiveData<List<DateModel>> = _daysList


    private val _selectedDate: MutableLiveData<Long?> = MutableLiveData()
    val selectedDate: LiveData<Long?> = _selectedDate

    private val _selectedTime: MutableLiveData<String?> = MutableLiveData()
    val selectedTime: LiveData<String?> = _selectedTime

    private val _timeSlots = MutableLiveData<List<String>>()
    val timeSlots: LiveData<List<String>> = _timeSlots


    private val _movies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val movies: LiveData<UIState<List<Doc>>> = _movies

    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
        MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
    val selectedMovie: LiveData<Doc?> = _selectedMovie

    init {
        generateDaysList()
        generateTimeList()
    }


    fun generateTimeList() {
        val timeSlots = mutableListOf<String>()
        val formatter =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) // Важно указать Locale

        for (hour in 10..22 step 2) { // Цикл от 10:00 до 22:00 с шагом 2 часа
            val time = LocalTime.of(hour, 0)
            timeSlots.add(time.format(formatter))
        }
        _timeSlots.postValue(timeSlots) // Используем postValue для обновления из фонового потока
        Log.d("MainVM", "Generated time slots: $timeSlots")
    }


    fun selectTime(time: String) {
        _selectedTime.value = time
        Log.d("MainVM", "Selected time: $time")
    }



    fun generateDaysList() {
        val days = mutableListOf<DateModel>()
        val today = LocalDate.now()

        for (i in 0 until 30) {
            val date = today.plusDays(i.toLong())
            val (dayOfWeek, dayOfMonth) = formatDate(date) // Получаем Pair

            days.add(
                DateModel(
                    dayOfWeek = dayOfWeek, // День недели
                    dayOfMonth = dayOfMonth, // Число и месяц
                    localDate = date
                )
            )
        }
        _daysList.value = days
    }

    fun selectDate(position: Int) {
        val currentList = _daysList.value
        _selectedDate.value = currentList?.get(position)?.localDate?.toEpochDay()
    }


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



    fun selectedMovie(movie: Doc) {
        _selectedMovie.value = movie

    }

    private fun formatDate(date: LocalDate): Pair<String, String> { // Возвращаем Pair
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar { if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth
        return Pair(dayOfWeek, "$dayOfMonth $month") // Возвращаем пару: день недели и "число месяц"
    }
}


