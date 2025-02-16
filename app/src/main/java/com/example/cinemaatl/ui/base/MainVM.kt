package com.example.cinemaatl.ui.base

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.MovieService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
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

//
//    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
//    val selectedSeats: LiveData<List<String>> = _selectedSeats

//    private val _movieId:MutableLiveData<Doc?> = MutableLiveData()
//    val movieId: LiveData<Doc?> = _movieId


    private val _userNickname: MutableLiveData<String?> = MutableLiveData()
    val userNickname: LiveData<String?> = _userNickname

    private val _userEmail: MutableLiveData<String?> = MutableLiveData()
    val userEmail:LiveData<String?> = _userEmail

//    private val _movieId :MutableLiveData<UIState<List<Doc?>>> = MutableLiveData()
//    val movieId: LiveData<UIState<List<Doc?>>> = _movieId
//
//
//    fun getMovieId(id:String){
//        viewModelScope.launch {
//            _.value = UIState.Loading(true)
//
//            val result = apiCall {
//                movieService.getActorByID(id = id)
//            }
//            when(result){
//                is ApiResult.Success -> {
//                    val movieId = result.data?.docs
//                    if (movieId.isNullOrEmpty()){
//                        _movieId.value = UIState.Error(null, "Movie Not Found")
//                    } else {
//                        _movieId.value = UIState.Success(movieId)
//                    }
//                }
//                is ApiResult.Error -> {
//                    _movieId.value = UIState.Error(
//                        result.error?.errorCode,
//                        result.error?.errorMessage
//                    )
//                }
//            }
//        }
//    }





    fun getCurrentUserEmail() {
        val currentUser = firebaseAuth.currentUser
        _userEmail.value = currentUser?.email ?: "Guest"
    }


    fun getCurrentNickname() {
    val currentUser = firebaseAuth.currentUser
    _userNickname.value = currentUser?.displayName ?: "Guest"
}


//    fun updateSelectedSeats(selectedSeats: List<String>) {
//        _selectedSeats.value = selectedSeats
//    }
    fun logOut(){
        firebaseAuth.signOut()
    }

    //список доступных дат
//    private val _daysList: MutableLiveData<List<DateModel>> = MutableLiveData()
//    val daysList: LiveData<List<DateModel>> = _daysList
//
//    //выбранная дата
//    private val _selectedDate: MutableLiveData<DateModel?> = MutableLiveData()
//    val selectedDate: LiveData<DateModel?> = _selectedDate
//
//    //выбранное время
//    private val _selectedTime: MutableLiveData<String?> = MutableLiveData()
//    val selectedTime: LiveData<String?> = _selectedTime
//
//    //список доступных временных слотов
//    private val _timeSlots = MutableLiveData<List<String>>()
//    val timeSlots: LiveData<List<String>> = _timeSlots



//    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
//        MutableLiveData<UIState<List<Doc>>>()
//    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies
//
//    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
//    val selectedMovie: LiveData<Doc?> = _selectedMovie
//
//
//    private val _searchResult: MutableLiveData<UIState<List<Doc>>> = MutableLiveData()
//    val searchResult: LiveData<UIState<List<Doc>>> = _searchResult
//
//    private val _searchQuery: MutableLiveData<String> = MutableLiveData()
//    val searchQuery: LiveData<String> = _searchQuery
//
//    private val _totalPrices: MutableLiveData<Int> = MutableLiveData()
//    val totalPrices: LiveData<Int> = _totalPrices
//
//    private val _saveTicketState: MutableLiveData<UIState<Unit>> = MutableLiveData()
//    val saveTicketState: LiveData<UIState<Unit>> = _saveTicketState
//
//
//    private val _ticketsState = MutableLiveData<UIState<List<TicketModel>>>()
//    val ticketsState: LiveData<UIState<List<TicketModel>>> = _ticketsState


//    init {
//        generateDaysList()
//        generateTimeList()

//    }

//    fun loadUserTickets() {
//        val currentUser = firebaseAuth.currentUser
//        if(currentUser != null){
//            _ticketsState.value = UIState.Loading(true) // show loading
//
//            firestore.collection("tickets")
//                .whereEqualTo("userId", currentUser.uid) // фильтр по АЙди пользователя
//                .get()
//                .addOnSuccessListener { result ->
//                    val tickets = result.map { document ->
//                        val ticket = document.toObject(TicketModel::class.java)
//                        ticket.copy(id = document.id) // Добавляем Firestore ID
//
//                    }
//                    _ticketsState.value = UIState.Success(tickets)
//                }
//                .addOnFailureListener { e ->
//                    Log.e("MainVM", "loadUserTickets: ${e.localizedMessage}")
//                    _ticketsState.value = UIState.Error(
//                        errorCode = 500,
//                        errorMessage = "Firestore Error: ${e.localizedMessage}"
//                    )
//                }
//        } else {
//            _ticketsState.value = UIState.Error(
//                errorCode = 401,
//                errorMessage = "User not Authenticated")
//        }
//    }

//    fun saveTicket(ticket: TicketModel) {
//        val currentUser = firebaseAuth.currentUser
//
//        if (currentUser != null) {
//            //Добавляем Айди юзера в билет
//            val ticketWithUser = ticket.copy(userId = currentUser.uid)
//            _saveTicketState.value = UIState.Loading(true)
//
//            firestore.collection("tickets")
//                .add(ticketWithUser)
//                .addOnSuccessListener { documentReference ->
//                    Log.d("MainVM", "Ticket saved with ID: ${documentReference.id}")
//
//                    // Обновляем объект TicketModel, добавляя Firestore ID
//                    val updatedTicket = ticketWithUser.copy(id = documentReference.id)
//
//                    // Сохраняем обновленный билет в LiveData
//                    _saveTicketState.value = UIState.Success(Unit)
//                }
//                .addOnFailureListener { e ->
//                    Log.e("MainVM", "Error saving ticket", e)
//
//                    _saveTicketState.value = UIState.Error(
//                        errorCode = 500,
//                        errorMessage = "Firestore Error: ${e.localizedMessage}"
//                    )
//                }
//        } else {
//            _saveTicketState.value = UIState.Error(
//                errorCode = 401,
//                errorMessage = "User not Authenticated"
//            )
//    }
//}
//    fun createTicket(): TicketModel {
//        return TicketModel(
//            id = "",
//            userId = "", // userId будет добавлен в saveTicket
//            movieId = selectedMovie.value?.id ?: "",
//            movieName = selectedMovie.value?.name ?: "",
//            moviePosterUrl = selectedMovie.value?.poster?.url ?: "",
//            selectedSeats = selectedSeats.value ?: emptyList(),
//            selectedDate = selectedDate.value?.let { data -> "${data.dayOfMonth}" }.toString(),
//            selectedTime = selectedTime.value ?: "",
//            totalPrice = totalPrices.value ?: 0
//        )
//    }
//
//    fun deleteTicket(ticket: TicketModel) {
//        val currentUser = firebaseAuth.currentUser
//        if(currentUser != null){
//            val ticketId = ticket.id
//            if(ticketId.isNullOrEmpty()){
//                Log.e("MainVM", "Ticket ID is null or empty. Cannot delete ticket.")
//                return
//            }
//            firestore.collection("tickets").document(ticketId)
//                .delete()
//                .addOnSuccessListener {
//                    Log.d("MainVM", "Ticket deleted successfully")
//                    loadUserTickets()
//                }
//                .addOnFailureListener {  e ->
//                    Log.e("MainVM", "Error deleting ticket", e)
//                }
//
//        }
//
//    }

//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun generateTimeList() {
//        val timeSlots = mutableListOf<String>()
//        val formatter =
//            DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) // Важно указать Locale
//
//        for (hour in 10..22 step 2) { // Цикл от 10:00 до 22:00 с шагом 2 часа
//            val time = LocalTime.of(hour, 0)
//            timeSlots.add(time.format(formatter))
//        }
//        _timeSlots.postValue(timeSlots) // Используем postValue для обновления из фонового потока
//        Log.d("MainVM", "Generated time slots: $timeSlots")
//    }
//
//
//    fun selectTime(time: String) {
//        Log.d("MainVM", "Selected time: $time")
//        _selectedTime.value = time
//
//    }
//    fun getTotalPrice(price: Int) {
//        _totalPrices.value = price
//    }
//
//    fun setSearchQuery(query: String) {
//        _searchQuery.value = query
//    }
//
//
//
//    fun generateDaysList() {
//        val days = mutableListOf<DateModel>()
//        val today = LocalDate.now()
//
//        for (i in 0 until 30) {
//            val date = today.plusDays(i.toLong())
//            val (dayOfWeek, dayOfMonth) = formatDate(date) // Получаем Pair
//
//            days.add(
//                DateModel(
//                    dayOfWeek = dayOfWeek, // День недели
//                    dayOfMonth = dayOfMonth, // Число и месяц
//                    localDate = date
//                )
//            )
//        }
//        _daysList.value = days
//    }
//
//    fun selectDate(position: Int) {
//        val currentList = _daysList.value
//        val selectedDate = currentList?.get(position)
//        Log.d("MainVM", "Selected Date: ${selectedDate?.dayOfMonth} ${selectedDate?.dayOfWeek}")
//        _selectedDate.value = selectedDate
////        _selectedDate.value = currentList?.get(position)?.localDate?.toEpochDay()
////        Log.d("MainVM", "Selected Date: $selectedDate")
//    }
//
//    fun searchAllMovies(query: String){
//        viewModelScope.launch {
//            _searchResult.value = UIState.Loading(true)
//
//            val result = apiCall {
//                movieService.searchMovies(query = query)
//            }
//            when(result){
//                is ApiResult.Success -> {
//                    val movieList = result.data?.docs
//                    if (movieList.isNullOrEmpty()){
//                        _searchResult.value = UIState.Error(null, "Movie not Founded")
//                    } else{
//
//                        _searchResult.value = UIState.Success(movieList)
//                    }
//                }
//                is ApiResult.Error ->{
//                    _searchResult.value = UIState.Error(
//                        result.error?.errorCode,
//                        result.error?.errorMessage
//                    )
//                }
//            }
//        }
//    }


//
//    fun selectedMovie(movie: Doc) {
//        Log.d("MainVM", "Selected movie: ${movie.name}")
//        Log.d("MainVM", "Persons list: ${movie.persons}") // Логируем список актеров
//        _selectedMovie.value = movie
//
//    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: LocalDate): Pair<String, String> { // Возвращаем Pair
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar { if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth
        return Pair(dayOfWeek, "$dayOfMonth $month") // Возвращаем пару: день недели и "число месяц"
    }
}


