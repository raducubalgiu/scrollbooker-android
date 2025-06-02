package com.example.scrollbooker.feature.products.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.feature.products.domain.model.Product
import com.example.scrollbooker.feature.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(userId: Int): Flow<PagingData<Product>> {
        return repository.getProducts(userId)
    }
}