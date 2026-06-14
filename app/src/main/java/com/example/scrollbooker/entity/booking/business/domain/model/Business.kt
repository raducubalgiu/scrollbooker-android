package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service

data class Business(
    val id: Int,
    val businessTypeId: Int,
    val ownerId: Int,
    val description: String?,
    val timezone: String,
    val address: String,
    val formattedAddress: String,
    val coordinates: BusinessCoordinates,
    val city: String,
    val countryCode: String,
    val mapUrl: String,
    val services: List<Service>,
    val schedules: List<Schedule>,
    val hasEmployees: Boolean
)