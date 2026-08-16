package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository

class GetPostLinkedProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(postId: Int): Result<List<Product>> {
        return runSuspendCatching {
            repository.getPostLinkedProducts(postId)
        }
    }
}