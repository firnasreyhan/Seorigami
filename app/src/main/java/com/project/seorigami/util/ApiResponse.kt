package com.project.seorigami.util

sealed interface ApiResponse<out T> {
    object Loading: ApiResponse<Nothing>

    class Error(
        val message: String
    ): ApiResponse<Nothing>

    class Success<T>(
        val data: T
    ): ApiResponse<T>
}