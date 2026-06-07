package com.example.scrollbooker.entity.search.domain.model

data class SearchUser(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float,
    val isBusinessOrEmployee: Boolean
)