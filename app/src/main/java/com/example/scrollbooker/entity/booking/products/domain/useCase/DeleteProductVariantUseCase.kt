package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductVariantUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int, variantId: Int): Result<Unit> {
        return runSuspendCatching {
            repository.deleteVariant(productId, variantId)
        }
    }
}