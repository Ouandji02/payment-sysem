package com.example.paymentsystem.domain.model

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    class Success<out T>(val data: String?) : Response<T>()
    class Error(val message: String) : Response<Nothing>()
}
