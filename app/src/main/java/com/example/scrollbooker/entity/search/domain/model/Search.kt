package com.example.scrollbooker.entity.search.domain.model
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

data class Search (
    val type: String,
    val label: String,
    val user: UserSocial?,
    val service: SearchServiceBusinessType?,
    val businessType: SearchServiceBusinessType?,
)

data class SearchServiceBusinessType(
    val id: Int,
    val name: String
)