package com.example.scrollbooker.core.util

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object VideoPlayerCache {
    private const val MAX_CACHE_SIZE = 100L * 1024L * 1024L // 100MB

    lateinit var simpleCache: SimpleCache
    lateinit var cacheDataSourceFactory: DataSource.Factory
    private lateinit var databaseProvider: DatabaseProvider

    fun init(context: Context) {
        if(::simpleCache.isInitialized) return

        databaseProvider = StandaloneDatabaseProvider(context)

        val cacheDir = File(context.cacheDir, "exo_cache")
        val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)

        simpleCache = SimpleCache(cacheDir, evictor, databaseProvider)
        cacheDataSourceFactory = getFactory(context)
    }

    fun getFactory(context: Context): DataSource.Factory {
        val upstreamFactory = DefaultDataSource.Factory(context)
        return CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}