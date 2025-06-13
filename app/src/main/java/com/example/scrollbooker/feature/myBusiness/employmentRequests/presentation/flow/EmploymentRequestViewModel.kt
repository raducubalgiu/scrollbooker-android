package com.example.scrollbooker.feature.myBusiness.employmentRequests.presentation.flow

import androidx.lifecycle.ViewModel
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequestCreate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class EmploymentRequestViewModel @Inject constructor(): ViewModel() {
    private val _professionId = MutableStateFlow<Int?>(null)
    val professionId: StateFlow<Int?> = _professionId

    private val _employeeId = MutableStateFlow<Int?>(null)
    val employeeId: StateFlow<Int?> = _employeeId

    private val _consentId = MutableStateFlow<Int?>(null)
    val consentId: StateFlow<Int?> = _consentId

    fun assignProfession(professionId: Int) {
        _professionId.value = professionId
    }

    fun assignEmployee(id: Int) {
        _employeeId.value = id
    }

    fun assignConsent(id: Int?) {
        _consentId.value = id
    }

    fun buildEmploymentRequest(): EmploymentRequestCreate {
        return EmploymentRequestCreate(
            employeeId = _employeeId.value ?: error("Employee missing"),
            professionId = _professionId.value ?: error("Profession missing"),
            consentId = _consentId.value ?: error("Consent missing ")
        )
    }
}