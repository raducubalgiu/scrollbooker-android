package com.example.scrollbooker.feature.profile.data.remote

import com.google.gson.annotations.SerializedName

//    { "id":1,
//    "username":"radu",
//    "fullname":"Raducu Balgiu",
//    "avatar":null,
//    "business_id":null,
//    "business_type_id":null,
//    "email":"radu@yahoo.com",
//    "counters":{"user_id":1,"followings_count":0,"followers_count":0,"products_count":0,"posts_count":0,"ratings_count":0,"ratings_average":5},
//    "profession":"Creator"}

data class UserDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,
    val username: String,
    val email: String,
    val avatar: String?,

    @SerializedName("business_id")
    val businessId: Int?,

    @SerializedName("business_type_id")
    val businessTypeId: Int?,

    val counters: CountersDto,

    val bio: String?,
    val profession: String,

    @SerializedName("instant_booking")
    val instantBooking: Boolean,

    @SerializedName("date_of_birth")
    val dateOfBirth: String?,

    @SerializedName("phone_number")
    val phoneNumber: String?
)

data class CountersDto(
    @SerializedName("followings_count")
    val followingsCount: Int,

    @SerializedName("followers_count")
    val followersCount: Int,

    @SerializedName("products_count")
    val productsCount: Int,

    @SerializedName("posts_count")
    val postsCount: Int,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    @SerializedName("ratings_average")
    val ratingsAverage: Int
)