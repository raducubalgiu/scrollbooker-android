package com.example.scrollbooker.entity.booking.business.domain.useCase
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class GetBusinessByUserUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(): FeatureState<Business> {
        try {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.tag("Datastore").e("User Id not found in authDataStore")
                return FeatureState.Error()
            }

            val response = repository.getBusiness(userId)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Business").e("ERROR: on Fetching Business $e")
            return FeatureState.Error(e)
        }
    }
}