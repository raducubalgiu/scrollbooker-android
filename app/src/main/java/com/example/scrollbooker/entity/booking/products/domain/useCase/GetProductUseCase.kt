package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository

class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Result<Product> {
        return repository.getProduct(productId)
    }
}