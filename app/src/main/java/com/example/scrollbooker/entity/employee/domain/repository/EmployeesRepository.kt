package com.example.scrollbooker.entity.employee.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.employee.domain.model.Employee
import kotlinx.coroutines.flow.Flow

interface EmployeesRepository {
    fun getEmployees(businessId: Int): Flow<PagingData<Employee>>
}