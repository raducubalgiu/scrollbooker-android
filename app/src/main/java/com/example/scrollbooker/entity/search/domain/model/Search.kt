package com.example.scrollbooker.entity.search.domain.model
import com.example.scrollbooker.entity.search.data.remote.SearchTypeEnum
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Search (
    val type: SearchTypeEnum?,
    val label: String,
    val user: SearchUser?,
    val service: SearchServiceBusinessType?,
    val businessType: SearchServiceBusinessType?,
)

data class SearchUser(
    val id: Int,
    val fullname: String,
    val username: String,
    val profession: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: BigDecimal,
    val distance: BigDecimal?,

    @SerializedName("is_business_or_employee")
    val isBusinessOrEmployee: Boolean
)

data class SearchServiceBusinessType(
    val id: Int,
    val name: String
)