package com.example.scrollbooker.entity.booking.products.domain.useCase
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

//class UpdateProductUseCase @Inject constructor(
//    private val repository: ProductRepository
//) {
//    suspend operator fun invoke(
//        product: ProductCreateRequest,
//        filters: List<ProductFilterRequest>
//    ): Result<Product> = runCatching {
//        repository.updateProduct(product, filters)
//    }
//}