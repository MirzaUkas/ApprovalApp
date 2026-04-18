package com.mirz.common.utils.state

sealed class UiState<out T> {
    data object Initial : UiState<Nothing>()
    data class Success<out T>(val data: T?) : UiState<T>()
    data class Failed(val throwable: Throwable) : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
}