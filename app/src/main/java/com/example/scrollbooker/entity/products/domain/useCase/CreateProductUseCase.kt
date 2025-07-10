package com.example.scrollbooker.entity.products.domain.useCase

import com.example.scrollbooker.entity.products.domain.model.Product
import com.example.scrollbooker.entity.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.products.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        productCreate: ProductCreate,
        subFilters: List<Int>
    ): Result<Product> = runCatching {
        repository.createProduct(productCreate, subFilters)
    }
}