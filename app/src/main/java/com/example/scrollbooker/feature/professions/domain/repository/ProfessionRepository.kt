package com.example.scrollbooker.feature.professions.domain.repository

import com.example.scrollbooker.feature.professions.domain.model.Profession

interface ProfessionRepository {
    suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession>
}