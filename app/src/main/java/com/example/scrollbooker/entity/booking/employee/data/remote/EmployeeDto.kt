package com.example.scrollbooker.entity.booking.employee.data.remote

import com.google.gson.annotations.SerializedName

data class EmployeeDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val job: String,

    @SerializedName("hire_date")
    val hireDate: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("followers_count")
    val followersCount: Int,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    @SerializedName("products_count")
    val productsCount: Int,
)