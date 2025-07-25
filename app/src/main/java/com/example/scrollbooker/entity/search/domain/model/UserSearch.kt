package com.example.scrollbooker.entity.search.domain.model
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.google.gson.annotations.SerializedName

data class UserSearch(
    @SerializedName("recommended_businesses")
    val recommendedBusiness: List<RecommendedBusiness>,

    @SerializedName("recently_search")
    val recentlySearch: List<RecentlySearch>
)