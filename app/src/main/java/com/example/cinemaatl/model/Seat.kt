package com.example.cinemaatl.model

data class Seat(var status: SeatStatus,val name:String){

    enum class SeatStatus{
        AVIABLE,SELECTED,UNAVIABLE
    }
}
