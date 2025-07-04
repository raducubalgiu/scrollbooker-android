package com.example.scrollbooker.entity.currency.data.remote

import com.google.gson.annotations.SerializedName

data class UserCurrencyUpdateRequest(
    @SerializedName("currency_ids")
    val currencyIds: List<Int>
)