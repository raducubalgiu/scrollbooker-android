package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.products.domain.model.LinkedProducts
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository

class GetPostLinkedProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        postId: Int,
        allowFallback: Boolean,
        lat: Float? = null,
        lng: Float? = null
    ): Result<LinkedProducts> {
        return runSuspendCatching {
            repository.getPostLinkedProducts(postId, allowFallback, lat, lng)
        }
    }
}