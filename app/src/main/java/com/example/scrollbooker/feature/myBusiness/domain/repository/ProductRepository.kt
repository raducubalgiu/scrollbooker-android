package com.example.scrollbooker.feature.myBusiness.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.myBusiness.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(userId: Int): Flow<PagingData<Product>>
}