package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.products.data.remote.ProductVariantRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductVariantUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, request: ProductVariantRequest): Result<Product> {
        return runSuspendCatching {
            repository.createVariant(productId, request)
        }
    }
}