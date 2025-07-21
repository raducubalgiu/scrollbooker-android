package com.example.scrollbooker.entity.nomenclature.currency.data.mapper

import com.example.scrollbooker.entity.nomenclature.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.nomenclature.currency.domain.model.Currency

fun CurrencyDto.toDomain(): Currency {
    return Currency(
        id = id,
        name = name
    )
}