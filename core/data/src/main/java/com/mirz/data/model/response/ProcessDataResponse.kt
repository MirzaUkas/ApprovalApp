package com.mirz.data.model.response

import com.google.gson.annotations.SerializedName
import com.mirz.data.model.entity.ProcessEntity

data class ProcessDataResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("success") val success: Boolean? = null,
) {
    fun mapToEntity(approved: Boolean) = ProcessEntity(
        message = message.orEmpty()
    )
}