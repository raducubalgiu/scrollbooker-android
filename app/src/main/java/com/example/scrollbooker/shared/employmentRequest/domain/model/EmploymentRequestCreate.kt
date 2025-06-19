package com.example.scrollbooker.shared.employmentRequest.domain.model

data class EmploymentRequestCreate(
    val employeeId: Int,
    val professionId: Int,
    val consentId: Int
)