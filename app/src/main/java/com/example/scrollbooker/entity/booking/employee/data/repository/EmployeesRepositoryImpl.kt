package com.example.scrollbooker.entity.booking.employee.data.repository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.employee.data.remote.EmployeesApiService
import com.example.scrollbooker.entity.booking.employee.data.remote.EmployeesPagingSource
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.repository.EmployeesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmployeesRepositoryImpl @Inject constructor(
    private val api: EmployeesApiService
): EmployeesRepository {
    override fun getEmployees(businessId: Int): Flow<PagingData<Employee>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { EmployeesPagingSource(api, businessId) }
        ).flow
    }
}