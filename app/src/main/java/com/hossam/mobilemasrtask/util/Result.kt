package com.hossam.mobilemasrtask.util

sealed class Result <T>(val data: T? = null, val message: Int? = null) {
    class Loading<T>(data: T? = null) : Result<T>(data)
    class Success<T>(data: T) : Result<T>(data)
    class Error<T>(message: Int, data: T? = null) : Result<T>(data, message)
}