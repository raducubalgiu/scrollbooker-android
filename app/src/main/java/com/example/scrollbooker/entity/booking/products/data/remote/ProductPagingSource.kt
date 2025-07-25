package com.example.scrollbooker.entity.booking.products.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import timber.log.Timber
import java.lang.Exception

class ProductPagingSource(
    private val api: ProductsApiService,
    private val userId: Int,
    private val serviceId: Int
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        val limit = 10

        return try {
            val response = api.getUserProducts(userId, serviceId, page, limit)
            val products = response.results.map { it.toDomain() }

            val totalLoaded = page * limit
            val isLastPage = totalLoaded >= response.count

            LoadResult.Page(
                data = products,
                nextKey = if(isLastPage) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            Timber.tag("Paging Products").e("ERROR: on Loading Products $e")
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}