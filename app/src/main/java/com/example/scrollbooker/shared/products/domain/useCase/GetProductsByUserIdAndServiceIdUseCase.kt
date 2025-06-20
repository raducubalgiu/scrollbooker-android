package com.example.scrollbooker.shared.products.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.shared.products.domain.model.Product
import com.example.scrollbooker.shared.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsByUserIdAndServiceIdUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(userId: Int, serviceId: Int): Flow<PagingData<Product>> {
        return repository.getUserProducts(userId, serviceId)
    }
}