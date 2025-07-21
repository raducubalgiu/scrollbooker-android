package com.example.scrollbooker.entity.booking.employee.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.repository.EmployeesRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetEmployeesUseCase(
    private val authDataStore: AuthDataStore,
    private val repository: EmployeesRepository
) {
    operator fun invoke(): Flow<PagingData<Employee>> = flow {
        val businessId = authDataStore.getBusinessId().firstOrNull()

        if(businessId == null) {
            Timber.tag("Employees").e("Business Id not found in DataStore")
            throw IllegalStateException("Business Id not found in DataStore")
        }

        emitAll(repository.getEmployees(businessId))
    }
}