package com.example.scrollbooker.feature.myBusiness.data.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.myBusiness.data.remote.products.ProductsApiService
import com.example.scrollbooker.feature.myBusiness.domain.model.Product
import com.example.scrollbooker.feature.myBusiness.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ProductsApiService
): ProductRepository {
    override fun getProducts(userId: Int): Flow<PagingData<Product>> {
        TODO("Not yet implemented")
    }
}