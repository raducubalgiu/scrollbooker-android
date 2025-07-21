package com.example.scrollbooker.entity.booking.employmentRequest.domain.repository

import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestCreate

interface EmploymentRequestRepository {
    suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest>
    suspend fun createEmploymentRequest(requestCreateDto: EmploymentRequestCreate)
    suspend fun responseEmploymentRequest(status: EmploymentRequestStatusEnum, employmentId: Int)
}