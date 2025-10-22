package com.example.scrollbooker.core.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toTwoDecimals(): String {
    return this.setScale(2, RoundingMode.HALF_UP).toPlainString()
}