package com.example.scrollbooker.shared.currency.data.mapper
import com.example.scrollbooker.shared.currency.data.remote.CurrencyDto
import com.example.scrollbooker.shared.currency.domain.model.Currency

fun CurrencyDto.toDomain(): Currency {
    return Currency(
        id = id,
        name = name
    )
}