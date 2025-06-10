package com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.repository

import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.model.EmployeeDismissal
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.repository.EmployeeDismissalRepository
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.remote.EmployeesDismissalApiService
import javax.inject.Inject

class EmployeeDismissalRepositoryImpl @Inject constructor(
    private val employeesDismissalApiService: EmployeesDismissalApiService
): EmployeeDismissalRepository {
    override suspend fun getDismissalConsent(consentName: String): EmployeeDismissal {
        return employeesDismissalApiService.getEmployeeDismissalConsent(consentName).toDomain()
    }

}