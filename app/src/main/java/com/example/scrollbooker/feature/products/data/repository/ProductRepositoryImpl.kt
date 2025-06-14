package com.example.scrollbooker.feature.products.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.scrollbooker.feature.products.data.mappers.toDomain
import com.example.scrollbooker.feature.products.data.remote.ProductPagingSource
import com.example.scrollbooker.feature.products.domain.model.Product
import com.example.scrollbooker.feature.products.domain.repository.ProductRepository
import com.example.scrollbooker.feature.products.data.remote.ProductsApiService
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
}