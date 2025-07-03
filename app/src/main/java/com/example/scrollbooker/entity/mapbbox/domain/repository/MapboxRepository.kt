package com.example.scrollbooker.entity.mapbbox.domain.repository

import com.example.scrollbooker.entity.mapbbox.domain.model.Address

interface MapboxRepository {
    suspend fun searchAddress(query: String): List<Address>
}