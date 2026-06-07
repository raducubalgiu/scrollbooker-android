package com.example.scrollbooker.entity.search.data.mappers

import com.example.scrollbooker.entity.search.data.remote.SearchUserDto
import com.example.scrollbooker.entity.search.domain.model.SearchUser

fun SearchUserDto.toDomain(): SearchUser {
    return SearchUser(
        id = id,
        username = username,
        fullName = fullName,
        avatar = avatar,
        profession = profession,
        ratingsAverage = ratingsAverage,
        isBusinessOrEmployee = isBusinessOrEmployee
    )
}