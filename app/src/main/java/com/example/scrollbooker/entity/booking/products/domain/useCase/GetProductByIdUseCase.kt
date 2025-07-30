package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import timber.log.Timber

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): FeatureState<Product> {
        return try {
            val response = repository.getProduct(productId)
            FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Product").e("ERROR: on Fetching Product $e")
            FeatureState.Error(e)
        }
    }
}