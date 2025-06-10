package com.example.scrollbooker.feature.myBusiness.employees.domain.model

import com.google.gson.annotations.SerializedName

data class Employee(
    val username: String,
    val job: String,
    val hireDate: String,
    val followersCount: String,
    val ratingsCount: String,
    val ratingsAverage: String
)
