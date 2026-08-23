package com.example.scrollbooker.core.network.util

fun isTokenValid(token: String?): Boolean {
    val expiry = token?.let { decodeJwtExpiry(it) }
    return expiry != null && System.currentTimeMillis() < expiry
}