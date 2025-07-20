package com.example.scrollbooker.entity.search.data.mappers
import com.example.scrollbooker.entity.search.data.remote.SearchDto
import com.example.scrollbooker.entity.search.data.remote.SearchServiceBusinessTypeDto
import com.example.scrollbooker.entity.search.data.remote.SearchTypeEnum
import com.example.scrollbooker.entity.search.data.remote.SearchUserDto
import com.example.scrollbooker.entity.search.domain.model.Search
import com.example.scrollbooker.entity.search.domain.model.SearchServiceBusinessType
import com.example.scrollbooker.entity.search.domain.model.SearchUser

fun SearchDto.toDomain(): Search {
    return Search(
        type = SearchTypeEnum.fromKey(type),
        label = label,
        user = user?.toDomain(),
        service = service?.toDomain(),
        businessType = businessType?.toDomain()
    )
}

fun SearchUserDto.toDomain(): SearchUser {
    return SearchUser(
        id = id,
        fullname = fullname,
        username = username,
        profession = profession,
        avatar = avatar,
        ratingsAverage = ratingsAverage,
        distance = distance,
        isBusinessOrEmployee = isBusinessOrEmployee,
    )
}

fun SearchServiceBusinessTypeDto.toDomain(): SearchServiceBusinessType {
    return SearchServiceBusinessType(
        id = id,
        name = name
    )
}