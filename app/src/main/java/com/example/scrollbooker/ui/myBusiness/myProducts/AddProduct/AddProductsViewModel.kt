package com.example.scrollbooker.ui.myBusiness.myProducts.AddProduct

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.enums.FilterTypeEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetAllEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.data.remote.ProductCreateRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductFilterRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductOfferingRequest
import com.example.scrollbooker.entity.booking.products.data.remote.ProductVariantRequest
import com.example.scrollbooker.entity.booking.products.domain.useCase.CreateProductUseCase
import com.example.scrollbooker.entity.nomenclature.filter.domain.model.Filter
import com.example.scrollbooker.entity.nomenclature.filter.domain.useCase.GetFiltersByServiceUseCase
import com.example.scrollbooker.store.AuthDataStore
import com.example.scrollbooker.ui.myBusiness.myProducts.FilterSelection
import com.example.scrollbooker.ui.myBusiness.myProducts.SelectedFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddProductsViewModel @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val getAllEmployeesByOwnerUseCase: GetAllEmployeesByOwnerUseCase,
    private val getFiltersByServiceUseCase: GetFiltersByServiceUseCase,
    private val createProductUseCase: CreateProductUseCase,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _productState = MutableStateFlow(
        ProductState(
            variants = listOf(
                ProductVariantState(name = "", duration = "", offerings = emptyList())
            )
        )
    )
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<SelectedFilters>(emptyMap())
    val selectedFilters: StateFlow<SelectedFilters> = _selectedFilters.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _createSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val createSuccessEvent = _createSuccessEvent.asSharedFlow()

    val productValidation: StateFlow<AddProductValidation> = _productState
        .map { it.validate(context) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddProductValidation())

    val variantsValidation: StateFlow<List<AddProductVariantValidation>> = _productState
        .map { state -> state.variants.map { it.validate(context) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees = authDataStore.getBusinessOwnerId()
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { ownerId ->
            flow {
                emit(FeatureState.Loading)
                try {
                    val employeeList = withVisibleLoading { getAllEmployeesByOwnerUseCase(ownerId) }
                    initializeOfferingsFromEmployees(employeeList)

                    emit(FeatureState.Success(employeeList))
                } catch (e: Exception) {
                    Timber.tag("Employees").e(e, "ERROR: on Fetching employees failed")
                    emit(FeatureState.Error(e))
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureState.Loading)

    private val _currentServiceId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val filters: StateFlow<FeatureState<List<Filter>>> = _currentServiceId
        .flatMapLatest { serviceId ->
            flow {
                if (serviceId == null) {
                    emit(FeatureState.Success(emptyList()))
                    return@flow
                }
                emit(FeatureState.Loading)
                val result = getFiltersByServiceUseCase(serviceId)
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeatureState.Success(emptyList())
        )

    private fun initializeOfferingsFromEmployees(employees: List<Employee>) {
        _productState.update { current ->
            val variantZero = current.variants.getOrNull(0)

            // Punct de control: Dacă lista de oferte conține deja elemente, OPRIM re-inițializarea!
            if (variantZero != null && variantZero.offerings.isNotEmpty()) {
                return@update current
            }

            // Altfel, dacă e goală (prima deschidere a ecranului), o populăm normal
            val initialOfferings = employees.map { employee ->
                ProductOfferingState(
                    userId = employee.id,
                    fullName = employee.fullName,
                    isSelected = false
                )
            }

            val updatedVariants = current.variants.mapIndexed { index, variant ->
                if (index == 0) variant.copy(offerings = initialOfferings) else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    // Funcție pentru a bifa / debifa un angajat din listă
    fun toggleOfferingSelection(offeringIndex: Int, isSelected: Boolean) {
        _productState.update { current ->
            val updatedVariants = current.variants.mapIndexed { vIndex, variant ->
                if (vIndex == 0) {
                    val updatedOfferings = variant.offerings.mapIndexed { oIndex, offering ->
                        if (oIndex == offeringIndex) offering.copy(isSelected = isSelected) else offering
                    }
                    variant.copy(offerings = updatedOfferings)
                } else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    fun updateOfferingPrices(offeringIndex: Int, price: String, discount: String, priceWithDiscount: String) {
        _productState.update { current ->
            val updatedVariants = current.variants.mapIndexed { vIndex, variant ->
                if (vIndex == 0) {
                    val updatedOfferings = variant.offerings.mapIndexed { oIndex, offering ->
                        if (oIndex == offeringIndex) {
                            offering.copy(price = price, discount = discount, priceWithDiscount = priceWithDiscount)
                        } else offering
                    }
                    variant.copy(offerings = updatedOfferings)
                } else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    fun updateVariantDuration(duration: String) {
        _productState.update { current ->
            val updatedVariants = current.variants.mapIndexed { index, variant ->
                if (index == 0) variant.copy(duration = duration) else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    fun setName(name: String) {
        _productState.update { current -> current.copy(name = name) }
    }

    fun setDescription(description: String) {
        _productState.update { current -> current.copy(description = description) }
    }

    fun setServiceDomainId(domainId: String) {
        _productState.update { current -> current.copy(serviceDomainId = domainId) }
    }

    fun setServiceId(serviceId: String) {
        _productState.update { current -> current.copy(serviceId = serviceId) }
        _selectedFilters.value = emptyMap()
        _currentServiceId.value = serviceId.toIntOrNull()
    }

    fun setCurrencyId(currencyId: String) {
        _productState.update { current -> current.copy(currencyId = currencyId) }
    }

    fun updateVariant(variantIndex: Int, name: String, duration: String) {
        _productState.update { current ->
            val updatedVariants = current.variants.mapIndexed { index, variant ->
                if (index == variantIndex) variant.copy(name = name, duration = duration) else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    fun updateOffering(variantIndex: Int, offeringIndex: Int, price: String, discount: String, priceWithDiscount: String) {
        _productState.update { current ->
            val updatedVariants = current.variants.mapIndexed { vIndex, variant ->
                if (vIndex == variantIndex) {
                    val updatedOfferings = variant.offerings.mapIndexed { oIndex, offering ->
                        if (oIndex == offeringIndex) {
                            offering.copy(price = price, discount = discount, priceWithDiscount = priceWithDiscount)
                        } else offering
                    }
                    variant.copy(offerings = updatedOfferings)
                } else variant
            }
            current.copy(variants = updatedVariants)
        }
    }

    fun setSingleOption(filterId: Int, subFilterId: Int?) {
        _selectedFilters.update { current ->
            if (subFilterId == null) current - filterId
            else current + (filterId to FilterSelection.Options(setOf(subFilterId)))
        }
    }

    fun toggleMultiOption(filterId: Int, subFilterId: Int) {
        _selectedFilters.update { current ->
            val prev = (current[filterId] as? FilterSelection.Options)?.ids.orEmpty()
            val next = if (subFilterId in prev) prev - subFilterId else prev + subFilterId
            if (next.isEmpty()) current - filterId
            else current + (filterId to FilterSelection.Options(next))
        }
    }

    fun setRange(filterId: Int, from: BigDecimal?, to: BigDecimal?) {
        _selectedFilters.update { current ->
            if (from == null && to == null) current - filterId
            else current + (filterId to FilterSelection.Range(minim = from, maxim = to))
        }
    }

    fun createProduct() {
        viewModelScope.launch {
            val state = _productState.value
            val globalValidation = state.validate(context)

            if (!globalValidation.isValid) {
                Timber.tag("Create Product").w("Formularul conține erori de validare.")
                return@launch
            }

            _isSaving.value = true

            try {
                val businessId = authDataStore.getBusinessId().firstOrNull()
                val currentUserId = authDataStore.getUserId().firstOrNull()

                if (businessId == null || currentUserId == null) {
                    throw IllegalStateException("Business Id or User Id not found in Auth Data Store")
                }

                val filters: List<ProductFilterRequest> =
                    _selectedFilters.value.entries.mapNotNull { (filterId, selection) ->
                        when (selection) {
                            is FilterSelection.Options -> {
                                if (selection.ids.isEmpty()) return@mapNotNull null
                                ProductFilterRequest(
                                    filterId = filterId,
                                    subFilterIds = selection.ids.toList(),
                                    type = FilterTypeEnum.OPTIONS.key,
                                    minim = null,
                                    maxim = null,
                                    isNotApplicable = false
                                )
                            }
                            is FilterSelection.Range -> {
                                if (selection.minim == null && selection.maxim == null) return@mapNotNull null
                                ProductFilterRequest(
                                    filterId = filterId,
                                    subFilterIds = emptyList(),
                                    type = FilterTypeEnum.RANGE.key,
                                    minim = selection.minim,
                                    maxim = selection.maxim,
                                    isNotApplicable = false
                                )
                            }
                        }
                    }

                val productCreateRequest = ProductCreateRequest(
                    name = state.name,
                    description = state.description.ifBlank { null },
                    serviceDomainId = state.serviceDomainId.toIntOrNull() ?: 0,
                    serviceId = state.serviceId.toIntOrNull() ?: 0,
                    businessId = businessId,
                    currencyId = state.currencyId.toIntOrNull() ?: 0,
                    variants = state.variants.map { variantState ->
                        ProductVariantRequest(
                            name = variantState.name,
                            duration = variantState.duration.toIntOrNull() ?: 0,
                            offerings = variantState.offerings
                                .filter { it.isSelected }
                                .distinctBy { it.userId }
                                .map { offeringState ->
                                    ProductOfferingRequest(
                                        userId = offeringState.userId,
                                        price = offeringState.price.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                                        discount = offeringState.discount.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                                        priceWithDiscount = offeringState.priceWithDiscount.toBigDecimalOrNull() ?: BigDecimal.ZERO
                                    )
                                }
                        )
                    }
                )

                val response = withVisibleLoading {
                    createProductUseCase(productCreateRequest, filters)
                }

                response
                    .onSuccess {
                        _isSaving.value = false
                        _createSuccessEvent.tryEmit(Unit)
                    }
                    .onFailure { e ->
                        if (e is retrofit2.HttpException) {
                            // Citim JSON-ul de eroare trimis de server în body
                            val errorBody = e.response()?.errorBody()?.string()
                            Timber.tag("Create Product").e("SERVER VALIDATION ERROR (422): $errorBody")
                        } else {
                            Timber.tag("Create Product").e(e, "ERROR: on Creating Product via API")
                        }
                        _isSaving.value = false
                    }

            } catch (e: Exception) {
                Timber.tag("Create Product").e(e, "ERROR: Unexpected exception during assembly")
                _isSaving.value = false
            }
        }
    }
}