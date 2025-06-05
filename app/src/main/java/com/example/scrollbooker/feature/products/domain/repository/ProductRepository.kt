package com.example.scrollbooker.feature.products.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getUserProducts(userId: Int): Flow<PagingData<Product>>
}