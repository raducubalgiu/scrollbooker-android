package com.example.scrollbooker.feature.profile.data.repository

import com.example.scrollbooker.feature.profile.data.mappers.toDomain
import com.example.scrollbooker.feature.profile.data.remote.UserProfileApiService
import com.example.scrollbooker.feature.profile.domain.model.UserProfile
import com.example.scrollbooker.feature.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val apiService: UserProfileApiService
): UserProfileRepository {
    override suspend fun getUserProfile(userId: Int): UserProfile {
        return apiService.getUserProfile(userId).toDomain()
    }

}