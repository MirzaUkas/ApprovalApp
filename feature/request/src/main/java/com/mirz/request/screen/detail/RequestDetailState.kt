package com.mirz.request.screen.detail

import androidx.compose.runtime.Immutable
import com.mirz.common.utils.state.UiState
import com.mirz.data.model.entity.ProcessEntity
import com.mirz.data.model.entity.RequestEntity

@Immutable
data class RequestDetailState(
    val requestState: UiState<RequestEntity> = UiState.Initial,
    val processState: UiState<ProcessEntity> = UiState.Initial,
    val shouldNavigateBack: Boolean = false,
)
