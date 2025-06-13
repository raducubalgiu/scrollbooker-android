package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.repository

import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequest

interface EmploymentRequestRepository {
    suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest>
    suspend fun createEmploymentRequest(requestCreateDto: EmploymentRequestCreateDto)
}