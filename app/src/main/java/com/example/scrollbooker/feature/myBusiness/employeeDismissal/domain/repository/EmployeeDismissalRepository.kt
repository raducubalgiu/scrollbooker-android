package com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.repository

import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.model.EmployeeDismissal

interface EmployeeDismissalRepository {
    suspend fun getDismissalConsent(consentName: String): EmployeeDismissal
}