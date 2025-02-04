package com.shaheer.adecadeofmovies.ui.models

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val throwable: Throwable, val data: T? = null) : Result<T>()
    data class Loading<out T>(val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[throwable=$throwable, data=$data]"
            is Loading -> "Loading[data=$data]"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

val Result<*>.data
    get() = if(this is Result.Success && data != null) data else null