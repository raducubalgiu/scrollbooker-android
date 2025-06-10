package com.example.scrollbooker.feature.professions.data.repository

import com.example.scrollbooker.feature.professions.data.mappers.toDomain
import com.example.scrollbooker.feature.professions.data.remote.ProfessionsApiService
import com.example.scrollbooker.feature.professions.domain.model.Profession
import com.example.scrollbooker.feature.professions.domain.repository.ProfessionRepository
import javax.inject.Inject

class ProfessionRepositoryImpl @Inject constructor(
    private val apiService: ProfessionsApiService
): ProfessionRepository {
    override suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession> {
        return apiService.getProfessionsByBusinessType(businessTypeId).map { it.toDomain() }
    }
}