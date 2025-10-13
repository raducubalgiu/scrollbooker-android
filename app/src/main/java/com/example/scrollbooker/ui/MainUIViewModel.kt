package com.example.scrollbooker.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.useCase.GetUserAppointmentsNumberUseCase
import com.example.scrollbooker.entity.search.domain.model.UserSearch
import com.example.scrollbooker.entity.search.domain.useCase.CreateUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.DeleteUserSearchUseCase
import com.example.scrollbooker.entity.search.domain.useCase.GetUserSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainUIViewModel @Inject constructor(
    private val getUserAppointmentsNumberUseCase: GetUserAppointmentsNumberUseCase,
    private val getUserSearchUseCase: GetUserSearchUseCase,
    private val createUserSearchUseCase: CreateUserSearchUseCase,
    private val deleteUserSearchUseCase: DeleteUserSearchUseCase,
): ViewModel() {
    // Appointments State
    private val _appointmentsState = MutableStateFlow<Int>(0)
    val appointmentsState: StateFlow<Int> = _appointmentsState.asStateFlow()

    // Notifications State
    private val _notificationsState = MutableStateFlow<Int>(0)
    val notificationsState: StateFlow<Int> = _notificationsState.asStateFlow()

    private val _userSearch = MutableStateFlow<FeatureState<UserSearch>>(FeatureState.Loading)
    val userSearch: StateFlow<FeatureState<UserSearch>> = _userSearch


    fun increaseAppointmentsNumber() {
        _appointmentsState.value = _appointmentsState.value + 1
    }

    fun decreaseAppointmentsNumber() {
        if(_appointmentsState.value > 0) {
            _appointmentsState.value = _appointmentsState.value - 1
        }
    }

    private fun loadUserSearch() {
        viewModelScope.launch {
            _userSearch.value = FeatureState.Loading
            _userSearch.value = getUserSearchUseCase(
                lng = 44.45050f,
                lat = 25.993102f,
                timezone = "Europe/Bucharest"
            )
        }
    }

    fun createSearch(keyword: String) {
        viewModelScope.launch {
            val response = createUserSearchUseCase(keyword)

            response
                .onFailure { e ->
                    Timber.tag("Search").e("ERROR: on Creating User Search $e")
                }
                .onSuccess { newEntry ->
                    val current = _userSearch.value
                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = buildList {
                            add(newEntry)
                            addAll(current.data.recentlySearch)
                        }.take(20)

                        val updatedUserSearch = current.data.copy(
                            recentlySearch = updatedRecentlySearch
                        )

                        _userSearch.value = FeatureState.Success(updatedUserSearch)
                    }
                }
        }
    }

    fun deleteUserSearch(searchId: Int) {
        viewModelScope.launch {
            val response = deleteUserSearchUseCase(searchId)

            response
                .onFailure { e ->
                    Timber.tag("Search").e("ERROR: on Deleting User Search $e")
                }
                .onSuccess {
                    val current = _userSearch.value
                    if(current is FeatureState.Success) {
                        val updatedRecentlySearch = current.data.recentlySearch
                            .filterNot { it.id == searchId }

                        val updatedUserSearch = current.data.copy(
                            recentlySearch = updatedRecentlySearch
                        )

                        _userSearch.value = FeatureState.Success(updatedUserSearch)
                    }
                }
        }
    }

    private fun loadAppointmentsNumber() {
        viewModelScope.launch {
            _appointmentsState.value = getUserAppointmentsNumberUseCase()
        }
    }

    init {
        viewModelScope.launch {
            launch { loadAppointmentsNumber() }
            //launch { loadAllBusinessTypes() }
            //launch { loadAllBusinessDomains() }
            launch { loadUserSearch() }
        }
    }
}