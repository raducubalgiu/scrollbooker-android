package com.example.scrollbooker.entity.booking.products.data.repository
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.products.data.mappers.toDto
import com.example.scrollbooker.entity.booking.products.data.remote.AddProductFilterRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequestDto
import com.example.scrollbooker.entity.booking.products.data.remote.ProductsApiService
import com.example.scrollbooker.entity.booking.products.domain.model.BusinessServicesWithProducts
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductsApiService
): ProductRepository {
    override suspend fun getProductsByBusinessIdAndEmployeeId(
        businessId: Int,
        employeeId: Int?,
        onlyServicesWithProducts: Boolean
    ): List<BusinessServicesWithProducts> {
        return api.getProductsByBusinessIdAndEmployeeId(
            businessId, employeeId, onlyServicesWithProducts
        ).map { it.toDomain() }
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
        productCreate: ProductCreate,
        serviceDomainId: Int,
        filters: List<AddProductFilterRequest>
    ): Product {
        val request = ProductCreateRequestDto(
            product = productCreate.toDto(),
            serviceDomainId = serviceDomainId,
            filters = filters
        )
        return api.createProduct(request).toDomain()
    }

    override suspend fun updateProduct(
        productCreate: ProductCreate,
        serviceDomainId: Int,
        filters: List<AddProductFilterRequest>,
        productId: Int
    ): Product {
        val request = ProductCreateRequestDto(
            product = productCreate.toDto(),
            serviceDomainId = serviceDomainId,
            filters = filters
        )
        return api.updateProduct(productId, request).toDomain()
    }

    override suspend fun deleteProduct(productId: Int) {
        return api.deleteProduct(productId)
    }
}