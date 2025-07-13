package com.example.scrollbooker.entity.appointment.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.appointment.data.mappers.toDomain
import timber.log.Timber
import java.lang.Exception

class AppointmentPagingSource(
    private val api: AppointmentsApiService,
    private val asCustomer: Boolean
) : PagingSource<Int, Appointment>() {

    override fun getRefreshKey(state: PagingState<Int, Appointment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Appointment> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = if(page == 1) {
                withVisibleLoading { api.getUserAppointments(page, limit, asCustomer) }
            } else {
                api.getUserAppointments(page, limit, asCustomer)
            }

            val appointments = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = appointments,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Appointments").e("ERROR: on Loading Appointments $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}