package com.example.scrollbooker.feature.products.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.feature.products.data.mappers.toDomain
import com.example.scrollbooker.feature.products.domain.model.Product
import java.lang.Exception

class ProductPagingSource(
    private val api: ProductsApiService,
    private val userId: Int,
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        val limit = params.loadSize

        return try {
            val response = api.getUserProducts(userId, page, limit)
            val products = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = products,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }


}