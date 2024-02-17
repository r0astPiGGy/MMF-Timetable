package com.rodev.mmf_timetable.domain.service

sealed class ApiResult<T> {

    data class Success<T>(val data: T) : ApiResult<T>()

    class Exception<T>(val exception: Throwable): ApiResult<T>()

    class Failure<T>(val error: String) : ApiResult<T>()

}

inline fun <T, K> ApiResult<T>.transform(transformer: (T) -> K): ApiResult<K> {
    return when (this) {
        is ApiResult.Exception -> ApiResult.Exception(exception)
        is ApiResult.Failure -> ApiResult.Failure(error)
        is ApiResult.Success -> ApiResult.Success(transformer(data))
    }
}
