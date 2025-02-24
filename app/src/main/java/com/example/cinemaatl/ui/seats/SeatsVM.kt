package com.example.cinemaatl.ui.seats

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.UIState
import com.example.cinemaatl.model.DateModel
import com.example.cinemaatl.model.Seat
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

//
//    private val _seats: MutableLiveData<List<Seat>> = MutableLiveData()
//    val seats: LiveData<List<Seat>> = _seats

    private val _selectedSeats: MutableLiveData<List<String>> = MutableLiveData()
    val selectedSeats: LiveData<List<String>> = _selectedSeats

    //выбранная дата
    private val _selectedDate: MutableLiveData<DateModel?> = MutableLiveData()
    val selectedDate: LiveData<DateModel?> = _selectedDate

    //список доступных дат
    private val _daysList: MutableLiveData<List<DateModel>> = MutableLiveData()
    val daysList: LiveData<List<DateModel>> = _daysList



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
//package com.example.cinemaatl.ui.Seats
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.cinemaatl.model.DateModel
//import com.example.cinemaatl.model.Seat
//import dagger.hilt.android.lifecycle.HiltViewModel
//import java.time.LocalDate
//import java.time.format.TextStyle
//import java.util.Locale
//import javax.inject.Inject
//
//@HiltViewModel
//class SeatsVM @Inject constructor() : ViewModel() {
//
//    // Список мест
//    private val _seats = MutableLiveData<List<Seat>>()
//    val seats: LiveData<List<Seat>> = _seats
//
//    // Выбранные места
//    private val _selectedSeats = MutableLiveData<List<Seat>>()
//    val selectedSeats: LiveData<List<Seat>> = _selectedSeats
//
//    // Выбранная дата
//    private val _selectedDate = MutableLiveData<DateModel?>()
//    val selectedDate: LiveData<DateModel?> = _selectedDate
//
//    // Выбранное время
//    private val _selectedTime = MutableLiveData<String?>()
//    val selectedTime: LiveData<String?> = _selectedTime
//
//    // Общая стоимость
//    private val _totalPrice = MutableLiveData<Int>()
//    val totalPrice: LiveData<Int> = _totalPrice
//
//    init {
//        generateSeats()
//        generateDates()
//    }
//
//    // Генерация списка мест
//    private fun generateSeats() {
//        val seats = (0 until 60).map { index ->
//            val row = ('A' + index / 5).toString()
//            val seatNumber = (index % 5) + 1
//            val seatName = "$row$seatNumber"
//            val status = when (index) {
//                2, 20, 21, 33, 41, 42, 50 -> Seat.SeatStatus.UNAVIABLE
//                else -> Seat.SeatStatus.AVIABLE
//            }
//            Seat(status, seatName)
//        }
//        _seats.value = seats
//    }
//
//    // Генерация списка дат
//    private fun generateDates() {
//        val today = LocalDate.now()
//        val dates = (0 until 30).map { i ->
//            val date = today.plusDays(i.toLong())
//            formatDate(date)
//        }
//        _selectedDate.value = dates.firstOrNull()
//    }
//
//    // Форматирование даты
//    private fun formatDate(date: LocalDate): DateModel {
//        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//        val dayOfMonth = date.dayOfMonth.toString()
//        return DateModel(dayOfWeek, dayOfMonth, date)
//    }
//
//    // Выбор места
//    fun selectSeat(seat: Seat) {
//        val currentSelectedSeats = _selectedSeats.value.orEmpty().toMutableList()
//        if (seat.status == Seat.SeatStatus.AVIABLE) {
//            if (currentSelectedSeats.contains(seat)) {
//                currentSelectedSeats.remove(seat)
//            } else {
//                currentSelectedSeats.add(seat)
//            }
//            _selectedSeats.value = currentSelectedSeats
//            calculateTotalPrice()
//        }
//    }
//
//    // Выбор даты
//    fun selectDate(date: DateModel) {
//        _selectedDate.value = date
//    }
//
//    // Выбор времени
//    fun selectTime(time: String) {
//        _selectedTime.value = time
//    }
//
//    // Расчет общей стоимости
//    private fun calculateTotalPrice() {
//        val pricePerSeat = 500 // Цена за одно место
//        val total = (_selectedSeats.value?.size ?: 0) * pricePerSeat
//        _totalPrice.value = total
//    }
//}