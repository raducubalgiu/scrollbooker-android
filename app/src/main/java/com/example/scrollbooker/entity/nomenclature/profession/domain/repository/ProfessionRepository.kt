package com.example.scrollbooker.entity.nomenclature.profession.domain.repository

import com.example.scrollbooker.entity.nomenclature.profession.domain.model.Profession

interface ProfessionRepository {
    suspend fun getProfessionsByBusinessTypeId(businessTypeId: Int): List<Profession>
}