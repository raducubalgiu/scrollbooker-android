package com.example.scrollbooker.feature.myBusiness.products.domain.useCase

import com.example.scrollbooker.feature.myBusiness.products.domain.model.Product
import com.example.scrollbooker.feature.myBusiness.products.domain.repository.ProductRepository

class GetProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Result<Product> {
        return repository.getProduct(productId)
    }
}