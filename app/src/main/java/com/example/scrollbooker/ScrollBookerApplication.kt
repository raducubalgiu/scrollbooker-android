package com.example.scrollbooker

import android.app.Application
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.core.util.VideoPlayerCache
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ScrollBookerApplication: Application() {
    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        VideoPlayerCache.init(this)
        Timber.plant(Timber.DebugTree())
    }
}