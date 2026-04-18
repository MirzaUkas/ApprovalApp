package com.mirz.data.repository

import com.mirz.common.base.BaseRepository
import com.mirz.data.source.remote.ApiService
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RequestRepository, BaseRepository() {

    override fun getRequest() = collectApiResult(
        fetchApi = { apiService.getRequestData() },
        transformData = { it.mapToEntity() }
    )

    override fun processRequest(
        id: Int,
        isApproved: Boolean
    ) = collectApiResult(
        fetchApi = { apiService.processApproval(id, isApproved) },
        transformData = {
            it.mapToEntity(isApproved)
        }
    )
}