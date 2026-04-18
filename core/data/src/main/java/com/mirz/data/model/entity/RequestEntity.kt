package com.mirz.data.model.entity


data class RequestEntity(
    val id: Int,
    val title: String,
    val desc: String,
){
    companion object {
        fun default() = RequestEntity(id = 0, desc = "", title = "")
    }
}