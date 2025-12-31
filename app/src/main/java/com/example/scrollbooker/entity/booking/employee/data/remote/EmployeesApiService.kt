package com.example.scrollbooker.entity.booking.employee.data.remote

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

    @GET("businesses/owner/{ownerId}/employees")
    suspend fun getEmployeesByOwner(
        @Path("ownerId") ownerId: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PaginatedResponseDto<EmployeeDto>
}