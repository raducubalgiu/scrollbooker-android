package com.example.scrollbooker.entity.booking.products.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import timber.log.Timber
import java.lang.Exception

class GetProductsByAppointmentIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(appointmentId: Int): FeatureState<List<Product>> {
        return try {
            val response = repository.getProductsByAppointmentId(appointmentId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Products").e("ERROR: on Fetching Products By Appointment Id: $e")
            FeatureState.Error(e)
        }
    }
}