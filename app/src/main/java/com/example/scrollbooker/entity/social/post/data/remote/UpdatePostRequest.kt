package com.example.scrollbooker.entity.social.post.data.remote

import com.google.gson.annotations.SerializedName

data class UpdatePostRequest(
    val description: String? = null,

    @SerializedName("linked_product_ids")
    val linkedProductIds: List<Int>,
)