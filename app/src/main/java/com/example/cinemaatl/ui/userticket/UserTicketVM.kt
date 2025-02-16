package com.example.cinemaatl.ui.userticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.MovieService
import com.example.cinemaatl.UIState
import com.example.cinemaatl.model.DateModel
import com.example.cinemaatl.model.Doc
import com.example.cinemaatl.model.TicketModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserTicketVM
@Inject constructor(
    private val movieService: MovieService,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth// Интерфейс для запросов
) : ViewModel() {

    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
    val selectedSeats: LiveData<List<String>> = _selectedSeats


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


    private val _upcomingMovies: MutableLiveData<UIState<List<Doc>>> =
        MutableLiveData<UIState<List<Doc>>>()
    val upcomingMovies: LiveData<UIState<List<Doc>>> = _upcomingMovies

    private val _selectedMovie: MutableLiveData<Doc?> = MutableLiveData()
    val selectedMovie: LiveData<Doc?> = _selectedMovie


    private val _totalPrices: MutableLiveData<Int> = MutableLiveData()
    val totalPrices: LiveData<Int> = _totalPrices

    private val _saveTicketState: MutableLiveData<UIState<Unit>> = MutableLiveData()
    val saveTicketState: LiveData<UIState<Unit>> = _saveTicketState


    private val _ticketsState = MutableLiveData<UIState<List<TicketModel>>>()
    val ticketsState: LiveData<UIState<List<TicketModel>>> = _ticketsState


    fun loadUserTickets() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _ticketsState.value = UIState.Loading(true) // show loading

            firestore.collection("tickets")
                .whereEqualTo("userId", currentUser.uid) // фильтр по АЙди пользователя
                .get()
                .addOnSuccessListener { result ->
                    val tickets = result.map { document ->
                        val ticket = document.toObject(TicketModel::class.java)
                        ticket.copy(id = document.id) // Добавляем Firestore ID

                    }
                    _ticketsState.value = UIState.Success(tickets)
                }
                .addOnFailureListener { e ->
                    Log.e("MainVM", "loadUserTickets: ${e.localizedMessage}")
                    _ticketsState.value = UIState.Error(
                        errorCode = 500,
                        errorMessage = "Firestore Error: ${e.localizedMessage}"
                    )
                }
        } else {
            _ticketsState.value = UIState.Error(
                errorCode = 401,
                errorMessage = "User not Authenticated"
            )
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
                .addOnSuccessListener { documentReference ->
                    Log.d("MainVM", "Ticket saved with ID: ${documentReference.id}")

                    // Обновляем объект TicketModel, добавляя Firestore ID
                    val updatedTicket = ticketWithUser.copy(id = documentReference.id)

                    // Сохраняем обновленный билет в LiveData
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

    fun createTicket(): TicketModel {
        return TicketModel(
            id = "",
            userId = "", // userId будет добавлен в saveTicket
            movieId = selectedMovie.value?.id ?: "",
            movieName = selectedMovie.value?.name ?: "",
            moviePosterUrl = selectedMovie.value?.poster?.url ?: "",
            selectedSeats = selectedSeats.value ?: emptyList(),
            selectedDate = selectedDate.value?.let { data -> "${data.dayOfMonth}" }.toString(),
            selectedTime = selectedTime.value ?: "",
            totalPrice = totalPrices.value ?: 0
        )
    }

    fun deleteTicket(ticket: TicketModel) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val ticketId = ticket.id
            if (ticketId.isNullOrEmpty()) {
                Log.e("MainVM", "Ticket ID is null or empty. Cannot delete ticket.")
                return
            }
            firestore.collection("tickets").document(ticketId)
                .delete()
                .addOnSuccessListener {
                    Log.d("MainVM", "Ticket deleted successfully")
                    loadUserTickets()
                }
                .addOnFailureListener { e ->
                    Log.e("MainVM", "Error deleting ticket", e)
                }

        }

    }


    fun selectTime(time: String) {
        Log.d("MainVM", "Selected time: $time")
        _selectedTime.value = time

    }

    fun getTotalPrice(price: Int) {
        _totalPrices.value = price
    }


    fun selectDate(position: Int) {
        val currentList = _daysList.value
        val selectedDate = currentList?.get(position)
        Log.d("MainVM", "Selected Date: ${selectedDate?.dayOfMonth} ${selectedDate?.dayOfWeek}")
        _selectedDate.value = selectedDate
//        _selectedDate.value = currentList?.get(position)?.localDate?.toEpochDay()
//        Log.d("MainVM", "Selected Date: $selectedDate")
    }


    fun selectedMovie(movie: Doc) {
        Log.d("MainVM", "Selected movie: ${movie.name}")
        Log.d("MainVM", "Persons list: ${movie.persons}") // Логируем список актеров
        _selectedMovie.value = movie

    }

}