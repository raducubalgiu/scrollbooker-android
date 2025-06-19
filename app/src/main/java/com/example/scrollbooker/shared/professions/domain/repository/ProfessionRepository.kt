package com.example.scrollbooker.shared.professions.domain.repository

import com.example.scrollbooker.shared.professions.domain.model.Profession

interface ProfessionRepository {
    suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession>
}