package com.example.scrollbooker.entity.currency.data.mapper
import com.example.scrollbooker.entity.currency.data.remote.CurrencyDto
import com.example.scrollbooker.entity.currency.domain.model.Currency

fun CurrencyDto.toDomain(): Currency {
    return Currency(
        id = id,
        name = name
    )
}