package com.example.scrollbooker.entity.consent.domain.model

data class Consent(
    val id: Int,
    val name: String,
    val title: String,
    val text: String,
    val version: String,
    val createdAt: String
)