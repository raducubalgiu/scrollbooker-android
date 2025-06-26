package com.example.scrollbooker.entity.profession.data.repository

import com.example.scrollbooker.entity.profession.data.mappers.toDomain
import com.example.scrollbooker.entity.profession.data.remote.ProfessionsApiService
import com.example.scrollbooker.entity.profession.domain.model.Profession
import com.example.scrollbooker.entity.profession.domain.repository.ProfessionRepository
import javax.inject.Inject

class ProfessionRepositoryImpl @Inject constructor(
    private val apiService: ProfessionsApiService
): ProfessionRepository {
    override suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession> {
        return apiService.getProfessionsByBusinessType(businessTypeId).map { it.toDomain() }
    }
}