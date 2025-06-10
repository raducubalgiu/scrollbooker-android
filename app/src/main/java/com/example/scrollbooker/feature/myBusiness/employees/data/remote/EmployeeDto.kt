package com.example.scrollbooker.feature.myBusiness.employees.data.remote

import com.google.gson.annotations.SerializedName

data class EmployeeDto(
    val username: String,
    val job: String,

    @SerializedName("hire_date")
    val hireDate: String,

    @SerializedName("followers_count")
    val followersCount: String,

    @SerializedName("ratings_count")
    val ratingsCount: String,

    @SerializedName("ratings_average")
    val ratingsAverage: String
)