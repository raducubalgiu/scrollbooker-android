package com.example.scrollbooker.entity.search.domain.repository
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
interface SearchRepository {
    suspend fun searchUsers(query: String, roleClient: Boolean): List<UserSocial>
}