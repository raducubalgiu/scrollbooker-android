package com.example.scrollbooker.ui.myBusiness.myProducts.components

import java.math.BigDecimal
import java.math.RoundingMode

fun calculatePriceWithDiscount(
    price: String,
    discountPercent: String,
    scale: Int = 2,
    rounding: RoundingMode = RoundingMode.HALF_UP
): BigDecimal {
    val p = price.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val d = discountPercent.toBigDecimalOrNull() ?: BigDecimal.ZERO

    val dClamped = d.coerceIn(BigDecimal.ZERO, BigDecimal(100))

    val factor = BigDecimal.ONE.subtract(dClamped.divide(BigDecimal(100)))
    return p.multiply(factor).setScale(scale, rounding)
}