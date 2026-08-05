package com.example.scrollbooker.entity.onboarding.domain.useCase

import android.net.Uri
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectBusinessGalleryUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(businessId: Int, photos: List<Uri?>): Result<AuthState> {
        return runCatching {
            repository.collectBusinessGallery(businessId, photos)
        }
    }
}