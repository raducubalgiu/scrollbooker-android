package com.example.scrollbooker.entity.booking.employee.domain.model

data class Employee(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val job: String,
    val hireDate: String,
    val ratingsAverage: Float,
    val followersCount: Int,
    val ratingsCount: Int,
    val productsCount: Int
)
