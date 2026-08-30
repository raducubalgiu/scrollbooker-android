package com.example.scrollbooker.entity.booking.products.data.repository
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.products.data.remote.ProductBaseInfoUpdateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductVariantRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductWithFiltersCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductsApiService
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductsApiService
): ProductRepository {
    override suspend fun getProductsByBusinessIdAndEmployeeId(
        businessId: Int,
        employeeId: Int?,
        onlyServicesWithProducts: Boolean,
        productsLimitPerService: Int?
    ): UserProducts {
        return api.getProductsByBusinessIdAndEmployeeId(
            businessId, employeeId, onlyServicesWithProducts, productsLimitPerService
        ).toDomain()
    }

    override suspend fun getProductsByAppointmentId(appointmentId: Int): List<Product> {
        return api.getProductsByAppointmentId(appointmentId).map { it.toDomain() }
    }

    override suspend fun getPostLinkedProducts(postId: Int): List<Product> {
        return api.getPostLinkedProducts(postId).map { it.toDomain() }
    }

    override suspend fun getProduct(productId: Int): Product {
        return api.getProduct(productId).toDomain()
    }

    override suspend fun createProduct(
        product: ProductCreateRequest,
        filters: List<ProductFilterRequest>
    ): Product {
        val request = ProductWithFiltersCreateRequest(product, filters)
        return api.createProduct(request).toDomain()
    }

    override suspend fun deleteProduct(productId: Int) {
        return api.deleteProduct(productId)
    }

    override suspend fun updateProductBaseInfo(
        productId: Int,
        product: ProductBaseInfoUpdateRequest
    ): Product {
        return api.updateProduct(productId, product).toDomain()
    }

    override suspend fun createVariant(
        productId: Int,
        request: ProductVariantRequest
    ): Product {
        return api.createVariant(productId, request).toDomain()
    }

    override suspend fun updateVariant(
        productId: Int,
        variantId: Int,
        request: ProductVariantRequest
    ): Product {
        return api.updateVariant(productId, variantId, request).toDomain()
    }

    override suspend fun deleteVariant(productId: Int, variantId: Int) {
        return api.deleteVariant(productId, variantId)
    }
}