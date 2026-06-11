package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import timber.log.Timber
import java.lang.Exception

class GetProductsByBusinessIdAndEmployeeIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        businessId: Int,
        employeeId: Int?,
        onlyServicesWithProducts: Boolean = true,
        productsLimitPerService: Int?= null
    ): FeatureState<UserProducts> {
        return try {
            val response = repository.getProductsByBusinessIdAndEmployeeId(
                businessId = businessId,
                employeeId = employeeId,
                onlyServicesWithProducts = onlyServicesWithProducts,
                productsLimitPerService
            )

            FeatureState.Success(response)
        } catch (e: Exception) {
            Timber.tag("BookingProducts").e(e, "ERROR: on fetching products for business: $businessId")
            FeatureState.Error(e)
        }
    }
}