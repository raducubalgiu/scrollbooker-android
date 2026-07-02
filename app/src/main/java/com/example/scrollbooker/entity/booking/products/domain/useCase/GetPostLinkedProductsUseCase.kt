package com.example.scrollbooker.entity.booking.products.domain.useCase

import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import timber.log.Timber
import java.lang.Exception

class GetPostLinkedProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(postId: Int): Result<List<Product>> {
        return try {
            val response = repository.getPostLinkedProducts(postId)
            Result.success(response)
        } catch (e: Exception) {
            Timber.tag("Products").e(e, "ERROR: on Fetching Post Linked Products")
            Result.failure(e)
        }
    }
}