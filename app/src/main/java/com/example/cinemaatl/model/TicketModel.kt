package com.example.cinemaatl.model

data class TicketModel(
    val id:String = "",
    val userId: String = "",
    val movieId: String = "",
    val movieName: String = "",
    val moviePosterUrl: String = "",
    val selectedSeats: List<String> = emptyList(),
    val selectedDate: String = "",
    val selectedTime: String = "",
    val totalPrice:Int = 0,
)