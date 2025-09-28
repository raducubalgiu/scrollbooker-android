package com.example.scrollbooker.ui.onboarding.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.useCase.CollectClientBirthDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CollectClientBirthDateViewModel @Inject constructor(
    private val collectClientBirthDateUseCase: CollectClientBirthDateUseCase
): ViewModel() {
    val selectedDay = MutableStateFlow<String?>(null)
    val selectedMonth = MutableStateFlow<String?>(null)
    val selectedYear = MutableStateFlow<String?>(null)

    private val _isSaving = MutableStateFlow<FeatureState<Unit>?>(null)
    val isSaving: StateFlow<FeatureState<Unit>?> = _isSaving

    val isBirthDateValid: StateFlow<Boolean> = combine(
        selectedDay, selectedMonth, selectedYear
    ) { day, month, year ->
        !day.isNullOrBlank() && !month.isNullOrBlank() && !year.isNullOrBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        false
    )

    fun setSelectedDay(newDay: String?) {
        selectedDay.value = newDay
    }

    fun setSelectedMonth(newMonth: String?) {
        selectedMonth.value = newMonth
    }

    fun setSelectedYear(newYear: String?) {
        selectedYear.value = newYear
    }

    suspend fun collectUserBirthDate(): Result<AuthState> {
        val day = selectedDay.value?.toIntOrNull()
        val month = selectedMonth.value?.toIntOrNull()
        val year = selectedYear.value?.toIntOrNull()

        _isSaving.value = FeatureState.Loading

        val birthdate = if (day != null && month != null && year != null) {
            LocalDate.of(year, month, day).toString()
        } else null

        val result = withVisibleLoading { collectClientBirthDateUseCase(birthdate = birthdate) }

        result
            .onFailure { e ->
                _isSaving.value = FeatureState.Error(e)
                Timber.tag("Update birthdate").e("ERROR: on updating Birthdate $e")
            }
            .onSuccess {
                _isSaving.value = FeatureState.Success(Unit)
            }

        return result
    }
}