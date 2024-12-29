package com.example.cinemaatl.model

data class ErrorModel(
    val errorCode: Int?,           // Код ошибки, например, 404 для "not found" или 500 для внутренней ошибки сервера
    val errorMessage: String?,     // Сообщение об ошибке, которое сервер возвращает, например "City not found"
    val errorDescription: String?  // Дополнительная информация или описание, которое ты можешь добавить для более подробной отладки
)
