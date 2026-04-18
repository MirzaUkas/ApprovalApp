package com.mirz.data.model.response

import com.google.gson.annotations.SerializedName
import com.mirz.data.model.entity.RequestEntity

data class RequestDataResponse(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("desc") val desc: String? = null,
) {
    fun mapToEntity() = RequestEntity(id = id ?: 0, title = title.orEmpty(), desc = desc.orEmpty())
}