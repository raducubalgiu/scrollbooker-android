package com.example.scrollbooker.shared.currencies.data.mapper
import com.example.scrollbooker.shared.currencies.data.remote.CurrencyDto
import com.example.scrollbooker.shared.currencies.domain.model.Currency

fun CurrencyDto.toDomain(): Currency {
    return Currency(
        id = id,
        name = name
    )
}