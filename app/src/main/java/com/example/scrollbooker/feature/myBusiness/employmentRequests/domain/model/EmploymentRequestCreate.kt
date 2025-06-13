package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model

data class EmploymentRequestCreate(
    val employeeId: Int,
    val professionId: Int,
    val consentId: Int
)