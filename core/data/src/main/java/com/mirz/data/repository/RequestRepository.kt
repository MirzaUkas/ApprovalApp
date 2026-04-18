package com.mirz.data.repository

import com.mirz.common.utils.state.ApiState
import com.mirz.data.model.entity.ProcessEntity
import com.mirz.data.model.entity.RequestEntity
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    fun getRequest(id: Int): Flow<ApiState<RequestEntity>>

    fun processRequest(id: Int, isApproved: Boolean): Flow<ApiState<ProcessEntity>>
}