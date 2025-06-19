package com.example.scrollbooker.shared.employmentRequest.domain.repository

import com.example.scrollbooker.shared.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.shared.employmentRequest.domain.model.EmploymentRequest

interface EmploymentRequestRepository {
    suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest>
    suspend fun createEmploymentRequest(requestCreateDto: EmploymentRequestCreateDto)
}