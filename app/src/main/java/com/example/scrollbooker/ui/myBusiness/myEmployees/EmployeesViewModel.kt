package com.example.scrollbooker.ui.myBusiness.myEmployees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase
): ViewModel() {

    val employeesFlow: Flow<PagingData<Employee>> = getEmployeesUseCase()
        .cachedIn(viewModelScope)
}