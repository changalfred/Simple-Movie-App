package com.example.simplemovieapp.utilities

sealed class ResourceState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceState<T>(data)
    class Loading<T> : ResourceState<T>()
    class Error<T>(data: T? = null, message: String) : ResourceState<T>(data, message)
}
