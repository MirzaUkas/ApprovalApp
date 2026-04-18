package com.mirz.request.screen.detail

import androidx.lifecycle.SavedStateHandle
import com.mirz.common.utils.state.ApiState
import com.mirz.common.utils.state.UiState
import com.mirz.data.model.entity.ProcessEntity
import com.mirz.data.model.entity.RequestEntity
import com.mirz.data.repository.RequestRepository
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    // Relaxed: args (SavedStateHandle.toRoute) is lazy and never accessed in these tests
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Success getRequest`() = runTest {
        // Given
        val entity = RequestEntity(id = 1, title = "Title", desc = "Desc")
        val viewModel = buildViewModel(
            getResult = flow {
                emit(ApiState.Loading)
                delay(1) // suspend so collector observes Loading before Success arrives
                emit(ApiState.Success(entity))
            }
        )
        val states = mutableListOf<UiState<RequestEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.requestState) }
        }
        viewModel.getRequest(1)
        advanceUntilIdle()

        // Then
        assertTrue(states.any { it is UiState.Loading })
        assertTrue(states.any { it is UiState.Success && it.data == entity })
    }

    @Test
    fun `Error getRequest`() = runTest {
        // Given
        val error = RuntimeException("Network error")
        val viewModel = buildViewModel(
            getResult = flow {
                emit(ApiState.Loading)
                emit(ApiState.Error(error))
            }
        )

        val states = mutableListOf<UiState<RequestEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.requestState) }
        }

        viewModel.getRequest(1)
        advanceUntilIdle()

        // Then
        val failed = states.filterIsInstance<UiState.Failed>().firstOrNull()
        assertTrue("Expected a Failed state", failed != null)
        assertEquals(error.message, failed!!.throwable.message)
    }

    @Test
    fun `Success approveRequest`() = runTest {
        // Given
        val entity = ProcessEntity("Request approved successfully.")
        val viewModel = buildViewModel(
            processResult = flow {
                emit(ApiState.Loading)
                delay(1) // suspend so collector observes Loading before Success arrives
                emit(ApiState.Success(entity))
            }
        )
        val states = mutableListOf<UiState<ProcessEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.processState) }
        }
        viewModel.approveRequest(1)
        advanceUntilIdle()

        // Then
        assertTrue(states.any { it is UiState.Loading })
        assertTrue(states.any { it is UiState.Success && it.data == entity })
    }

    @Test
    fun `Error approveRequest`() = runTest {
        // Given
        val error = Exception("Approval failed: server could not process the request at this time.")
        val viewModel = buildViewModel(
            processResult = flow {
                emit(ApiState.Loading)
                emit(ApiState.Error(error))
            }
        )

        val states = mutableListOf<UiState<ProcessEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.processState) }
        }

        viewModel.approveRequest(1)
        advanceUntilIdle()

        // Then
        val failed = states.filterIsInstance<UiState.Failed>().firstOrNull()
        assertTrue("Expected a Failed state", failed != null)
        assertEquals(error.message, failed!!.throwable.message)
    }

    @Test
    fun `Success rejectRequest`() = runTest {
        // Given
        val entity = ProcessEntity("Request rejected successfully.")
        val viewModel = buildViewModel(
            processResult = flow {
                emit(ApiState.Loading)
                delay(1)
                emit(ApiState.Success(entity))
            }
        )

        val states = mutableListOf<UiState<ProcessEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.processState) }
        }

        viewModel.rejectRequest(1)
        advanceUntilIdle()

        // Then
        assertTrue(states.any { it is UiState.Loading })
        assertTrue(states.any { it is UiState.Success && it.data == entity })
    }

    @Test
    fun `Error rejectRequest`() = runTest {
        // Given
        val error = Exception("Rejection failed: unable to reject the request.")
        val viewModel = buildViewModel(
            processResult = flow {
                emit(ApiState.Loading)
                emit(ApiState.Error(error))
            }
        )
        val states = mutableListOf<UiState<ProcessEntity>>()

        // When
        backgroundScope.launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it.processState) }
        }

        viewModel.rejectRequest(1)
        advanceUntilIdle()

        // Then
        val failed = states.filterIsInstance<UiState.Failed>().firstOrNull()
        assertTrue("Expected a Failed state", failed != null)
        assertEquals(error.message, failed!!.throwable.message)
    }

    private fun buildViewModel(
        getResult: Flow<ApiState<RequestEntity>> = flow {},
        processResult: Flow<ApiState<ProcessEntity>> = flow {},
    ): RequestDetailViewModel {
        val repo = object : RequestRepository {
            override fun getRequest(id: Int) = getResult
            override fun processRequest(id: Int, isApproved: Boolean) = processResult
        }
        return RequestDetailViewModel(savedStateHandle, repo).also {
            // Swap IO dispatcher so collectApiAsUiState runs on the test scheduler,
            // making advanceUntilIdle() fully deterministic
            it.ioDispatcher = testDispatcher
        }
    }
}
