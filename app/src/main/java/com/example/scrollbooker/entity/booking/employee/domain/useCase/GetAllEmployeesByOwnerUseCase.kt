package com.example.scrollbooker.entity.booking.employee.domain.useCase

import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.repository.EmployeesRepository
import javax.inject.Inject

class GetAllEmployeesByOwnerUseCase @Inject constructor(
    private val repository: EmployeesRepository
) {
    suspend operator fun invoke(ownerId: Int): List<Employee> {
        return repository.getAllEmployeesByOwnerId(ownerId)
    }
}