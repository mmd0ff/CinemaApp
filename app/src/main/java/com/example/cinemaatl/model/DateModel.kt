package com.example.cinemaatl.model

import java.time.LocalDate


data class DateModel (
    val dayOfWeek: String, // Название дня недели
    val dayOfMonth: String ,
    val localDate: LocalDate // Число
 )