package com.example.scrollbooker.core.util
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object AppLocaleProvider {
    fun current(): Locale {
        val appLocales: LocaleListCompat = AppCompatDelegate.getApplicationLocales()
        return if (!appLocales.isEmpty) {
            appLocales[0]!!
        } else {
            Locale.getDefault()
        }
    }
}