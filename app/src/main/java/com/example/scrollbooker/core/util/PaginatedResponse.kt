package com.example.scrollbooker.core.util

data class PaginatedResponseDto<T>(
    val count: Int,
    val results: List<T>
)