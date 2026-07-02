package com.example.scrollbooker.components.customized.post.sheets.linkedProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetPostLinkedProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LinkedProductsViewModel @Inject constructor(
    private val getPostLinkedProductsUseCase: GetPostLinkedProductsUseCase
) : ViewModel() {

    private val _postId = MutableStateFlow<Int?>(null)
    val postId = _postId.asStateFlow()

    fun setPostId(newPostId: Int) {
        if (_postId.value != newPostId) _postId.value = newPostId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productsState: StateFlow<FeatureState<List<Product>>> = _postId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading { getPostLinkedProductsUseCase(id) }

                result
                    .onSuccess { products ->
                        emit(FeatureState.Success(products))
                    }
                    .onFailure { exception ->
                        emit(FeatureState.Error(exception))
                    }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Loading
        )
}