package com.example.scrollbooker.ui.editPost

import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts

sealed interface EditPostUiState {
    object Loading : EditPostUiState
    data class Success(
        val coverUrl: String?,
        val coverKey: String?,
        val description: String,
        val linkedProducts: Set<Product>,
        val catalogProducts: UserProducts?,
        val rating: Int = 0,
        val review: String = ""
    ) : EditPostUiState
    data class Error(val error: Throwable?) : EditPostUiState
}