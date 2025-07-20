package com.example.scrollbooker.entity.search.domain.repository
import com.example.scrollbooker.entity.search.domain.model.Search

interface SearchRepository {
    suspend fun search(query: String, lng: Float?, lat: Float?): List<Search>
}