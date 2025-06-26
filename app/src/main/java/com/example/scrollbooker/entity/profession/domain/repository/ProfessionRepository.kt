package com.example.scrollbooker.entity.profession.domain.repository

import com.example.scrollbooker.entity.profession.domain.model.Profession

interface ProfessionRepository {
    suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession>
}