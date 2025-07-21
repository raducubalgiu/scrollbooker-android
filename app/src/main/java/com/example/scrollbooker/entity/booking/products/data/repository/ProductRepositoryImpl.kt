package com.example.scrollbooker.entity.booking.products.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.products.data.mappers.toDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequestDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductPagingSource
import com.example.scrollbooker.entity.booking.products.data.remote.ProductsApiService
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductsApiService
): ProductRepository {
    override fun getUserProducts(userId: Int, serviceId: Int): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { ProductPagingSource(api, userId, serviceId) }
        ).flow
    }

    override suspend fun getProduct(productId: Int): Result<Product> = runCatching {
        api.getProduct(productId).toDomain()
    }

    override suspend fun createProduct(
        productCreate: ProductCreate,
        subFilters: List<Int>
    ): Product {
        val request = ProductCreateRequestDto(product = productCreate.toDto(), subFilters = subFilters)
        return api.createProduct(request).toDomain()
    }

    override suspend fun deleteProduct(productId: Int) {
        return api.deleteProduct(productId)
    }
}