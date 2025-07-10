package com.example.scrollbooker.entity.products.domain.useCase
import com.example.scrollbooker.entity.products.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: Int): Result<Unit> = runCatching {
        repository.deleteProduct(productId)
    }
}