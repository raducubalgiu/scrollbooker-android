package com.example.scrollbooker.entity.booking.employee.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.repository.EmployeesRepository
import kotlinx.coroutines.flow.Flow

class GetEmployeesByOwnerUseCase(
    private val repository: EmployeesRepository
) {
    operator fun invoke(ownerId: Int): Flow<PagingData<Employee>> {
        return repository.getEmployeesByOwnerId(ownerId)
    }
}