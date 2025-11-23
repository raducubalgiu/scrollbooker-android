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

fun BigDecimal.toFixedDecimals(decimals: Int = 2): String {
    val scaled = BigDecimal(this.toString()).setScale(decimals, RoundingMode.HALF_UP)
    return scaled.toPlainString()
}