package com.mirz.request.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.mirz.common.base.BaseViewModel
import com.mirz.data.repository.RequestRepository
import com.mirz.navigation.helper.RequestGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RequestDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: RequestRepository
) : BaseViewModel() {

    val args by lazy { savedStateHandle.toRoute<RequestGraph.RequestDetailRoute>(RequestGraph.RequestDetailRoute.Companion.typeMap) }

    private val _uiState = MutableStateFlow(RequestDetailState())
    val uiState = _uiState.asStateFlow()

    fun getRequest(id: Int) = collectApiAsUiState(
        response = repo.getRequest(id),
        updateState = { state ->
            _uiState.update { it.copy(requestState = state) }
        }
    )

    fun approveRequest(id: Int) =
        collectApiAsUiState(
            response = repo.processRequest(id, isApproved = true),
            updateState = { state ->
                _uiState.update {
                    it.copy(
                        processState = state
                    )
                }
            }
        )

    fun rejectRequest(id: Int) = collectApiAsUiState(
        response = repo.processRequest(id, isApproved = false),
        updateState = { state ->
            _uiState.update {
                it.copy(
                    processState = state
                )
            }
        }
    )

}