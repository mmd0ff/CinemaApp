package com.example.cinemaatl.ui.core


sealed class UIState<out T> {
    data class Success<T>(val data: T): UIState<T>()
    data class Loading(val isLoading: Boolean): UIState<Nothing>()
    data class Error(val errorCode: Int? = null, val errorMessage: String? = null, val errorResId:Int = 0): UIState<Nothing>()
}