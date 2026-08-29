package com.example.scrollbooker.entity.social.post.data.remote
import com.google.gson.annotations.SerializedName

data class CreatePostRequest(
    val description: String? = null,
    val provider: String,

    @SerializedName("provider_uid")
    val providerUid: String,

    @SerializedName("order_index")
    val orderIndex: Int = 0,

    @SerializedName("linked_product_ids")
    val linkedProductIds: List<Int>,

    @SerializedName("custom_cover")
    val customCover: String? = null,

    @SerializedName("service_domain_id")
    val serviceDomainId: Int? = null
)