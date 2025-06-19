package com.example.scrollbooker.feature.myBusiness.products.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.myBusiness.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getUserProducts(userId: Int, serviceId: Int): Flow<PagingData<Product>>
    suspend fun getProduct(productId: Int): Result<Product>
}