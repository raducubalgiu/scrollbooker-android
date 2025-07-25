package com.example.scrollbooker.entity.search.data.remote

import com.example.scrollbooker.entity.booking.business.data.remote.RecommendedBusinessDto
import com.google.gson.annotations.SerializedName

data class UserSearchDto(
    @SerializedName("recommended_businesses")
    val recommendedBusiness: List<RecommendedBusinessDto>,

    @SerializedName("recently_search")
    val recentlySearch: List<RecentlySearchDto>
)