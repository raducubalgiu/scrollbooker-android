package com.example.scrollbooker.ui.myBusiness.mySchedules.components

import com.example.scrollbooker.core.extensions.toLocalTimeOrNull

fun isScheduleValid(start: String?, end: String?): Boolean {
    val sRaw = start?.takeUnless { it == "null" }
    val eRaw = end?.takeUnless { it == "null" }

    if (sRaw == null && eRaw == null) return true
    if (sRaw == null || eRaw == null) return false

    val s = sRaw.toLocalTimeOrNull() ?: return false
    val e = eRaw.toLocalTimeOrNull() ?: return false

    return s.isBefore(e)
}