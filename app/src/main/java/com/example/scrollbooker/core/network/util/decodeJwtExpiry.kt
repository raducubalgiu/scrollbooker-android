package com.example.scrollbooker.core.network.util

import android.util.Base64
import com.google.common.base.Charsets
import org.json.JSONObject

fun decodeJwtExpiry(token: String): Long? {
    return try {
        val parts = token.split(".")
        if(parts.size != 3) return null
        val payload = String(
            Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP),
            charset= Charsets.UTF_8
        )
        val json = JSONObject(payload)
        val exp = json.optLong("exp", 0)
        if(exp == 0L) null else exp * 1000
    } catch (e: Exception) {
        null
    }
}