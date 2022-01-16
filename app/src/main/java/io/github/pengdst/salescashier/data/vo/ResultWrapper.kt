package io.github.pengdst.salescashier.data.vo

sealed class ResultWrapper<T> {
    data class Success<T>(val data: T, val message: String): ResultWrapper<T>()
    data class Error<T>(val message: String, val data: T? = null): ResultWrapper<T>()
}