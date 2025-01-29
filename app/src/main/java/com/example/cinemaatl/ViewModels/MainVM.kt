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
import com.example.cinemaatl.model.TicketModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainVM @Inject constructor(
    private val movieService: MovieService,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth// Интерфейс для запросов
) : ViewModel() {

    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
    val selectedSeats: LiveData<List<String>> = _selectedSeats

//    private val _selectedClickDate: MutableLiveData<List<String>> = MutableLiveData()
//    val  selectedClickDate: LiveData<List<String>> = _selectedClickDate

//    fun showSelectedDate(selectedDate: List<String>){
//        _selectedClickDate.value = selectedDate
//    }

    fun updateSelectedSeats(selectedSeats: List<String>) {
        _selectedSeats.value = selectedSeats
    }

    //список доступных дат
    private val _daysList: MutableLiveData<List<DateModel>> = MutableLiveData()
    val daysList: LiveData<List<DateModel>> = _daysList

    //выбранная дата
    private val _selectedDate: MutableLiveData<DateModel?> = MutableLiveData()
    val selectedDate: LiveData<DateModel?> = _selectedDate

    //выбранное время
    private val _selectedTime: MutableLiveData<String?> = MutableLiveData()
    val selectedTime: LiveData<String?> = _selectedTime

    //список доступных временных слотов
    private val _timeSlots = MutableLiveData<List<String>>()
    val timeSlots: LiveData<List<String>> = _timeSlots


    private val _movies: MutableLiveData<UIState<List<Doc>>> = MutableLiveData<UIState<List<Doc>>>()
    val movies: LiveData<UIState<List<Doc>>> = _movies

    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
        MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
    val selectedMovie: LiveData<Doc?> = _selectedMovie


    private val _searchResult: MutableLiveData<UIState<List<Doc>>> = MutableLiveData()
    val searchResult: LiveData<UIState<List<Doc>>> = _searchResult

    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
    val searchQuery: LiveData<String> = _searchQuery

    private val _totalPrices: MutableLiveData<Int> = MutableLiveData()
    val totalPrices: LiveData<Int> = _totalPrices

    private val _saveTicketState: MutableLiveData<UIState<Unit>> = MutableLiveData()
    val saveTicketState: LiveData<UIState<Unit>> = _saveTicketState


    private val _ticketsState = MutableLiveData<UIState<List<TicketModel>>>()
    val ticketsState: LiveData<UIState<List<TicketModel>>> = _ticketsState


    init {
        generateDaysList()
        generateTimeList()
    }

    fun loadUserTickets() {
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            _ticketsState.value = UIState.Loading(true) // show loading

            firestore.collection("tickets")
                .whereEqualTo("userId", currentUser.uid) // фильтр по АЙди пользователя
                .get()
                .addOnSuccessListener { result ->
                    val tickets = result.toObjects(TicketModel::class.java)
                    _ticketsState.value = UIState.Success(tickets)
                }
                .addOnFailureListener { e ->
                    Log.e("MainVM", "loadUserTickets: ${e.localizedMessage}")
                }
        } else {
            _ticketsState.value = UIState.Error(
                errorCode = 401,
                errorMessage = "User not Authenticated")
        }
    }

    fun saveTicket(ticket: TicketModel) {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            //Добавляем Айди юзера в билет
            val ticketWithUser = ticket.copy(userId = currentUser.uid)
            _saveTicketState.value = UIState.Loading(true)

            firestore.collection("tickets")
                .add(ticketWithUser)
                .addOnSuccessListener {
                    Log.d("MainVM", "Ticket saved with ID: ${it.id}")
                    _saveTicketState.value = UIState.Success(Unit)
                }
                .addOnFailureListener { e ->
                    Log.e("MainVM", "Error saving ticket", e)

                    _saveTicketState.value = UIState.Error(
                        errorCode = 500,
                        errorMessage = "Firestore Error: ${e.localizedMessage}"
                    )
                }
        } else {
            _saveTicketState.value = UIState.Error(
                errorCode = 401,
                errorMessage = "User not Authenticated"
            )
    }
}


    @RequiresApi(Build.VERSION_CODES.O)
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
        Log.d("MainVM", "Selected time: $time")
        _selectedTime.value = time

    }
    fun getTotalPrice(price: Int) {
        _totalPrices.value = price
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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
        val selectedDate = currentList?.get(position)
        Log.d("MainVM", "Selected Date: ${selectedDate?.dayOfMonth} ${selectedDate?.dayOfWeek}")
        _selectedDate.value = selectedDate
//        _selectedDate.value = currentList?.get(position)?.localDate?.toEpochDay()
//        Log.d("MainVM", "Selected Date: $selectedDate")
    }

    fun searchAllMovies(query: String){
        viewModelScope.launch {
            _searchResult.value = UIState.Loading(true)

            val result = apiCall {
                movieService.searchMovies(query = query)
            }
            when(result){
                is ApiResult.Success -> {
                    val movieList = result.data?.docs
                    if (movieList.isNullOrEmpty()){
                        _searchResult.value = UIState.Error(null, "Movie not Founded")
                    } else{

                        _searchResult.value = UIState.Success(movieList)
                    }
                }
                is ApiResult.Error ->{
                    _searchResult.value = UIState.Error(
                        result.error?.errorCode,
                        result.error?.errorMessage
                    )
                }
            }
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: LocalDate): Pair<String, String> { // Возвращаем Pair
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar { if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth
        return Pair(dayOfWeek, "$dayOfMonth $month") // Возвращаем пару: день недели и "число месяц"
    }
}


