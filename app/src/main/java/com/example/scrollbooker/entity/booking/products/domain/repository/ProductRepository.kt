package com.example.scrollbooker.entity.booking.products.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getUserProducts(userId: Int, serviceId: Int, employeeId: Int?): Flow<PagingData<Product>>
    suspend fun getProduct(productId: Int): Product
    suspend fun createProduct(productCreate: ProductCreate, subFilters: List<Int>): Product
    suspend fun deleteProduct(productId: Int)
}