package com.example.scrollbooker.shared.profession.domain.repository

import com.example.scrollbooker.shared.profession.domain.model.Profession

interface ProfessionRepository {
    suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession>
}