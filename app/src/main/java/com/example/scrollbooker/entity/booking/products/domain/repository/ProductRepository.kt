package com.example.scrollbooker.entity.booking.products.domain.repository
import com.example.scrollbooker.entity.booking.products.data.remote.AddProductFilterRequest
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.ProductCreate
import com.example.scrollbooker.entity.booking.products.domain.model.ProductSection

interface ProductRepository {
    suspend fun getUserProducts(userId: Int, serviceId: Int, employeeId: Int?): List<ProductSection>
    suspend fun getProductsByAppointmentId(appointmentId: Int): List<Product>
    suspend fun getProductsByPostId(postId: Int): List<Product>
    suspend fun getProduct(productId: Int): Product
    suspend fun createProduct(productCreate: ProductCreate, serviceDomainId: Int, filters: List<AddProductFilterRequest>): Product
    suspend fun deleteProduct(productId: Int)
}