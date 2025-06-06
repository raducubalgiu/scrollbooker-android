package com.example.scrollbooker.feature.products.domain.useCase

import com.example.scrollbooker.feature.products.domain.model.Product
import com.example.scrollbooker.feature.products.domain.repository.ProductRepository

class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Product {
        return repository.getProduct(productId)
    }
}