package com.example.scrollbooker.entity.booking.employee.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import timber.log.Timber
import java.lang.Exception

class EmployeesPagingSource(
    private val api: EmployeesApiService,
    private val businessId: Int,
) : PagingSource<Int, Employee>() {

    override fun getRefreshKey(state: PagingState<Int, Employee>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = withVisibleLoading {
                api.getEmployees(businessId, page, limit)
            }
            val employees = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = employees,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Employees").e("ERROR: on Loading Employees $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}