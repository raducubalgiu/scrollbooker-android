package com.example.scrollbooker.ui.appointments

import androidx.paging.PagingData
import androidx.paging.testing.LoadErrorHandler
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentLastMinuteRequest
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import io.mockk.mockk
import androidx.paging.testing.asSnapshot

class FakeAppointmentsRepository : AppointmentRepository {
    val calls = mutableListOf<Boolean?>()

    override fun getUserAppointments(asCustomer: Boolean?): Flow<PagingData<Appointment>> {
        calls += asCustomer

        val data = fakeAppointmentsList
        return flowOf(PagingData.from(data))
    }

    override suspend fun createScrollBookerAppointment(appointmentCreate: AppointmentScrollBookerCreate) {
        TODO("Not yet implemented")
    }
    override suspend fun createOwnClientAppointment(appointmentCreate: AppointmentOwnClientCreate) {
        TODO("Not yet implemented")
    }
    override suspend fun createLastMinuteAppointment(lastMinuteRequest: AppointmentLastMinuteRequest) {
        TODO("Not yet implemented")
    }
    override suspend fun blockAppointments(request: AppointmentBlockRequest) {
        TODO("Not yet implemented")
    }
    override suspend fun getUserAppointmentsNumber(): Int {
        TODO("Not yet implemented")
    }
    override suspend fun cancelAppointment(appointmentId: Int, message: String) {
        TODO("Not yet implemented")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AppointmentsViewModelTest {

    @Test
    fun `appointments emits items and calls usecase with correct asCustomer`() = runTest {
        val repo = FakeAppointmentsRepository()
        val getUserAppointments = GetUserAppointmentsUseCase(repo)

        val vm = AppointmentsViewModel(
            getUserAppointmentsUseCase = getUserAppointments,
            deleteAppointmentUseCase = mockk(relaxed = true),
            createWrittenReviewUseCase = mockk(relaxed = true),
            updateWrittenReviewUseCase = mockk(relaxed = true),
            deleteWrittenReviewUseCase = mockk(relaxed = true),
        )

        // 1) snapshot-ul inițial (init + onStart emit(Unit))
        val first = vm.appointments.asSnapshot(this as LoadErrorHandler)
        assert(first.map { it.id } == listOf(1, 2))
        assert(repo.calls.first() == null)

        // 2) schimbăm filtrul + reload
        vm.loadAppointments(asCustomer = true)

        val second = vm.appointments.asSnapshot(this)
        assert(second.map { it.id } == listOf(1, 2))
        assert(repo.calls == listOf(null, true))
    }
}