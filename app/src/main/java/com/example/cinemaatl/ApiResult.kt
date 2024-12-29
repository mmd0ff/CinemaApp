package com.example.cinemaatl

import com.example.cinemaatl.model.ErrorModel

sealed class ApiResult<out T> {
    data class Success<T>(val data: T?): ApiResult<T>()
    data class Error(val error: ErrorModel?): ApiResult<Nothing>()
}