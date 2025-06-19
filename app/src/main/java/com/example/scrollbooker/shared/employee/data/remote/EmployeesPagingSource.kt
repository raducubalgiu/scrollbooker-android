package com.example.scrollbooker.shared.employee.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.shared.employee.data.mappers.toDomain
import com.example.scrollbooker.shared.employee.domain.model.Employee
import kotlinx.coroutines.delay
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
            delay(300)
            val response = api.getEmployees(businessId, page, limit)
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