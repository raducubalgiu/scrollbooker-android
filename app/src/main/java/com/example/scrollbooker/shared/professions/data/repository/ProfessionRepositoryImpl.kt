package com.example.scrollbooker.shared.professions.data.repository

import com.example.scrollbooker.shared.professions.data.mappers.toDomain
import com.example.scrollbooker.shared.professions.data.remote.ProfessionsApiService
import com.example.scrollbooker.shared.professions.domain.model.Profession
import com.example.scrollbooker.shared.professions.domain.repository.ProfessionRepository
import javax.inject.Inject

class ProfessionRepositoryImpl @Inject constructor(
    private val apiService: ProfessionsApiService
): ProfessionRepository {
    override suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession> {
        return apiService.getProfessionsByBusinessType(businessTypeId).map { it.toDomain() }
    }
}