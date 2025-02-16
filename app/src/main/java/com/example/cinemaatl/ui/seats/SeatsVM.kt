package com.example.cinemaatl.ui.seats

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.UIState
import com.example.cinemaatl.model.DateModel
import com.example.cinemaatl.model.TicketModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SeatsVM
@Inject constructor(

) : ViewModel() {

    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
    val selectedSeats: LiveData<List<String>> = _selectedSeats

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


    private val _totalPrices: MutableLiveData<Int> = MutableLiveData()
    val totalPrices: LiveData<Int> = _totalPrices
//
//    private val _saveTicketState: MutableLiveData<UIState<Unit>> = MutableLiveData()
//    val saveTicketState: LiveData<UIState<Unit>> = _saveTicketState
//
//
//    private val _ticketsState = MutableLiveData<UIState<List<TicketModel>>>()
//    val ticketsState: LiveData<UIState<List<TicketModel>>> = _ticketsState


    fun updateSelectedSeats(selectedSeats: List<String>) {
        _selectedSeats.value = selectedSeats
    }


    init {
        generateDaysList()
        generateTimeList()

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


    private fun formatDate(date: LocalDate): Pair<String, String> { // Возвращаем Pair
        val dayOfWeek =
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val dayOfMonth = date.dayOfMonth
        return Pair(dayOfWeek, "$dayOfMonth $month") // Возвращаем пару: день недели и "число месяц"
    }
}
