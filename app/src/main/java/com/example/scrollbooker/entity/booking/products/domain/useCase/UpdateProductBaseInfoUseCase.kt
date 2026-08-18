package com.example.scrollbooker.entity.booking.products.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.products.data.remote.ProductBaseInfoUpdateRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductBaseInfoUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(
        productId: Int,
        request: ProductBaseInfoUpdateRequest
    ): Result<Product> = runSuspendCatching {
        repository.updateProductBaseInfo(productId, request)
    }
}