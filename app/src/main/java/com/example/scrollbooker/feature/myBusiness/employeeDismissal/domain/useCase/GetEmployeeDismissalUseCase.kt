package com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.model.EmployeeDismissal
import com.example.scrollbooker.feature.myBusiness.employeeDismissal.domain.repository.EmployeeDismissalRepository
import timber.log.Timber
import java.lang.Exception

class GetEmployeeDismissalUseCase(
    private val repository: EmployeeDismissalRepository
) {
    suspend operator fun invoke(): FeatureState<EmployeeDismissal> {
        val consentName = "EMPLOYEE_DISMISSAL_SENT_BY_BUSINESS"

        return try {
            val response = repository.getDismissalConsent(consentName)
            Timber.tag("Employment Dismissal").e("Response: $response")
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Employment Dismissal").e("ERROR: on Fetching Employment Dismissal $e")
            return FeatureState.Error(e)
        }
    }
}