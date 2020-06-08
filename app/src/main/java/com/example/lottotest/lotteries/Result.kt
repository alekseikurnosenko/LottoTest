package com.example.lottotest.lotteries

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
}