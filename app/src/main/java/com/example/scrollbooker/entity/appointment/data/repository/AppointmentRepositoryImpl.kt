package com.example.scrollbooker.entity.appointment.data.repository
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentPagingSource
import com.example.scrollbooker.entity.appointment.data.remote.AppointmentsApiService
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.appointment.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val api : AppointmentsApiService
): AppointmentRepository {
    override fun getUserAppointments(asCustomer: Boolean): Flow<PagingData<Appointment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { AppointmentPagingSource(api, asCustomer) }
        ).flow
    }

}