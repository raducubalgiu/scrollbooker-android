package com.example.scrollbooker.entity.booking.products.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import timber.log.Timber
import java.lang.Exception

class GetProductsByUserIdAndServiceIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(userId: Int, serviceId: Int, employeeId: Int?): FeatureState<List<ProductSection>> {
        return try {
            val response = repository.getUserProducts(userId, serviceId, employeeId)
            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("Products").e(e, "ERROR: on Fetching Products By User Id and Service Id")
            FeatureState.Error(e)
        }
    }
}