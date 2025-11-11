package com.example.scrollbooker.core.extensions

import java.math.BigDecimal
import java.math.RoundingMode

fun Float.toDecimals(decimals: Int = 2): String {
    if(!this.isFinite()) return this.toString()
    val scaled = BigDecimal(this.toString()).setScale(decimals, RoundingMode.HALF_UP)
    val stripped = scaled.stripTrailingZeros()
    return if (stripped.scale() <= 0) {
        stripped.toBigInteger().toString()
    } else {
        stripped.toPlainString()
    }
}

fun Float.toFixedDecimals(decimals: Int = 2): String {
    if(!this.isFinite()) return this.toString()
    val scaled = BigDecimal(this.toString()).setScale(decimals, RoundingMode.HALF_UP)
    return scaled.toPlainString()
}