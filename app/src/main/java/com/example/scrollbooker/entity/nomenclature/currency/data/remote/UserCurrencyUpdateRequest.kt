package com.example.scrollbooker.entity.nomenclature.currency.data.remote

import com.google.gson.annotations.SerializedName

data class UserCurrencyUpdateRequest(
    @SerializedName("currency_ids")
    val currencyIds: List<Int>
)