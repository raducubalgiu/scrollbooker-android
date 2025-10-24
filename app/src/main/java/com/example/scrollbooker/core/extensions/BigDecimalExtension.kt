package com.example.scrollbooker.core.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.toTwoDecimals(): String {
    val scaled = this.setScale(2, RoundingMode.HALF_UP)
    return if(scaled.stripTrailingZeros().scale() <= 0) {
        scaled.toBigInteger().toString()
    } else {
        scaled.toPlainString()
    }
}