package com.example.scrollbooker.feature.myBusiness.employees.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.myBusiness.employees.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeesRepository {
    fun getEmployees(businessId: Int): Flow<PagingData<Employee>>
}