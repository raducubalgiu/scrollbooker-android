package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.repository

import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.list.EmploymentRequest

interface EmploymentRequestRepository {
    suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest>
}