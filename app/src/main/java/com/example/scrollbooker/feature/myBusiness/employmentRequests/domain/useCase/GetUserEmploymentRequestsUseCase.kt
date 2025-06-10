package com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.list.EmploymentRequest
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.repository.EmploymentRequestRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

class GetEmploymentRequestsUseCase(
    private val authDataStore: AuthDataStore,
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(): FeatureState<List<EmploymentRequest>> {
        val userId = authDataStore.getUserId().firstOrNull()

        if(userId == null) {
            Timber.tag("Employment Requests").e("User Id not found in DataStore")
            return FeatureState.Error()
        }

        return try {
            val response = repository.getUserEmploymentRequests(userId)
            return FeatureState.Success(response)
        } catch(e: Exception) {
            Timber.tag("Employment Requests").e("ERROR: on Fetching Employment Requests $e")
            return FeatureState.Error(e)
        }
    }
}