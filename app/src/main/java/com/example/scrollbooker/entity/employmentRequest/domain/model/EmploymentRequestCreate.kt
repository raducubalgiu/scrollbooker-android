package com.example.scrollbooker.entity.employmentRequest.domain.model

data class EmploymentRequestCreate(
    val employeeId: Int,
    val professionId: Int,
    val consentId: Int
)