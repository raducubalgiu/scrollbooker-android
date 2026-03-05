package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.entity.booking.products.data.remote.AddProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        productCreate: ProductCreate,
        serviceDomainId: Int,
        filters: List<AddProductFilterRequest>,
        productId: Int,
    ): Result<Product> = runCatching {
        repository.updateProduct(productCreate, serviceDomainId, filters, productId)
    }
}