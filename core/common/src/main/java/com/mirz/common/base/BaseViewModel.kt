package com.mirz.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirz.common.utils.state.ApiState
import com.mirz.common.utils.state.ApiState.Error
import com.mirz.common.utils.state.ApiState.Loading
import com.mirz.common.utils.state.ApiState.Success
import com.mirz.common.utils.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {
    private var apiJob: Job? = null

    /**
     * Collects API responses as UI states and updates the UI state accordingly.
     * @param response The flow of API responses to collect.
     * @param resetState If true, resets the UI state to initial after a successful or failed state.
     * @param updateState A lambda function to update the UI state.
     */
    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        resetState: Boolean = true,
        updateState: (UiState<T>) -> Unit
    ) {
        apiJob?.cancel()
        apiJob = viewModelScope.launch(IO) {
            response.map { apiState ->
                when (apiState) {
                    is Loading -> UiState.Loading
                    is Success -> UiState.Success(apiState.data)
                    is Error -> UiState.Failed(apiState.throwable)
                }
            }.collectLatest { uiState ->
                updateState(uiState)
                if (resetState && (uiState is UiState.Success || uiState is UiState.Failed)) {
                    delay(300)
                    updateState(UiState.Initial)
                }
            }
        }
    }

    /**
     * Handles the UI state by executing the appropriate action based on the state.
     * @param onSuccess Action to execute when the state is successful.
     * @param onLoading Action to execute when the state is loading.
     * @param onFailed Action to execute when the state has failed.
     */
    fun <T> UiState<T>.handleUiState(
        onSuccess: (T) -> Unit = {},
        onLoading: () -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) = when (this) {
        is UiState.Success -> data?.let { onSuccess(it) }
        is UiState.Loading -> onLoading()
        is UiState.Failed -> onFailed(throwable)
        is UiState.Initial -> Unit
    }

    fun <T> UiState<T>.isSuccess() = this is UiState.Success
    fun <T> UiState<T>.isFailed() = this is UiState.Failed
    fun <T> UiState<T>.isLoading() = this is UiState.Loading
    fun <T> UiState<T>.isInitial() = this is UiState.Initial
}