package com.example.scrollbooker.core.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Float.toTwoDecimals(): String {
    if(!this.isFinite()) return this.toString()
    val scaled = BigDecimal(this.toString()).setScale(2, RoundingMode.HALF_UP)
    val stripped = scaled.stripTrailingZeros()
    return if (stripped.scale() <= 0) {
        stripped.toBigInteger().toString()
    } else {
        stripped.toPlainString()
    }
}