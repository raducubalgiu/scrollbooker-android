package com.example.scrollbooker.entity.search.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SearchDto (
    val type: String,

    val label: String,
    val user: SearchUserDto?,
    val service: SearchServiceBusinessTypeDto?,

    @SerializedName("business_type")
    val businessType: SearchServiceBusinessTypeDto?,
)

data class SearchUserDto(
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

data class SearchServiceBusinessTypeDto(
    val id: Int,
    val name: String
)