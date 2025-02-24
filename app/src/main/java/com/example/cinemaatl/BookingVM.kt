package com.example.cinemaatl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.model.DateModel
import com.example.cinemaatl.model.Doc
import com.example.cinemaatl.model.TicketModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class BookingVM @Inject constructor(

) : ViewModel() {

    private val _daysList: MutableLiveData<List<DateModel>> = MutableLiveData()
    val daysList: LiveData<List<DateModel>> = _daysList

    private val _selectedDate: MutableLiveData<DateModel?> = MutableLiveData()
    val selectedDate: LiveData<DateModel?> = _selectedDate

    private val _timeSlots = MutableLiveData<List<String>>()
    val timeSlots: LiveData<List<String>> = _timeSlots

    private val _selectedTime: MutableLiveData<String?> = MutableLiveData()
    val selectedTime: LiveData<String?> = _selectedTime

    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
    val selectedSeats: LiveData<List<String>> = _selectedSeats

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> = _totalPrice

    private val _createdTicket: MutableLiveData<TicketModel?> = MutableLiveData()
    val createdTicket: LiveData<TicketModel?> = _createdTicket

    init {
        generateDaysList()
        generateTimeList()
    }


    fun selectTime(time: String) {
        _selectedTime.value = time
    }

    fun getTotalPrice(price: Int) {
        _totalPrice.value = price
    }

    fun updateSelectedSeats(selectedSeats: List<String>) {
        _selectedSeats.value = selectedSeats
    }


    fun createTicket(movie: Doc) {


        val ticket = TicketModel(
            id = "",
            userId = "", // Будет добавлен в saveTicket
            movieId = movie.id ?: "",
            movieName = movie.name ?: "",
            moviePosterUrl = movie.poster?.url ?: "",
            selectedSeats = _selectedSeats.value ?: emptyList(),
            selectedDate = _selectedDate.value?.let { data -> "${data.dayOfMonth}" } ?: "",
            selectedTime = _selectedTime.value ?: "",
            totalPrice = _totalPrice.value ?: 0
        )
        _createdTicket.value = ticket
    }


    fun generateDaysList() {
        val days = mutableListOf<DateModel>()
        val today = LocalDate.now()
        for (i in 0 until 30) {
            val date = today.plusDays(i.toLong())
            val (dayOfWeek, dayOfMonth) = formatDate(date)
            days.add(DateModel(dayOfWeek, dayOfMonth, date))
        }
        _daysList.value = days
    }


    fun generateTimeList() {
        val timeSlots = mutableListOf<String>()
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        for (hour in 10..22 step 2) {
            val time = LocalTime.of(hour, 0)
            timeSlots.add(time.format(formatter))
        }
        _timeSlots.value = timeSlots
    }

    fun selectDate(position: Int) {
        val currentList = _daysList.value
        val selectedDate = currentList?.get(position)
        _selectedDate.value = selectedDate
    }


    private fun formatDate(date: LocalDate): Pair<String, String> {
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .replaceFirstChar { it.titlecase(Locale.getDefault()) }
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth
        return Pair(dayOfWeek, "$dayOfMonth $month")
    }
}