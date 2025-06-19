package com.example.scrollbooker.shared.employee.data.remote

import com.example.scrollbooker.core.util.PaginatedResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EmployeesApiService {
    @GET("businesses/{businessId}/employees")
    suspend fun getEmployees(
        @Path("businessId") businessId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<EmployeeDto>
}