package com.example.scrollbooker.entity.employmentRequest.domain.repository

import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequest

interface EmploymentRequestRepository {
    suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest>
    suspend fun createEmploymentRequest(requestCreateDto: EmploymentRequestCreateDto)
}