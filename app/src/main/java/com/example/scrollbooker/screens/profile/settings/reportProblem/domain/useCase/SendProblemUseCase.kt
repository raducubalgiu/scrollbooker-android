package com.example.scrollbooker.screens.profile.settings.reportProblem.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.screens.profile.settings.reportProblem.domain.repository.ReportProblemRepository
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

class SendProblemUseCase @Inject constructor(
    private val authDataStore: AuthDataStore,
    private val repository: ReportProblemRepository
){
    suspend operator fun invoke(text: String): FeatureState<Unit> {
        return try {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.Forest.tag("Report Problem").e("ERROR: User Id Not Found in DataStore")
                return FeatureState.Error()
            }

            repository.sendReportProblem(text, userId)
            return FeatureState.Success(Unit)

        } catch (error: Exception) {
            Timber.Forest.tag("Report Problem").e("ERROR: on Sending Problem $error")
            return FeatureState.Error(error)
        }
    }
}