package com.example.scrollbooker.components.customized.post

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
    private const val MAX_CACHE_SIZE = 250L * 1024L * 1024L

    lateinit var simpleCache: SimpleCache
    lateinit var cacheDataSourceFactory: DataSource.Factory
    private lateinit var databaseProvider: DatabaseProvider

    fun init(context: Context) {
        if (::simpleCache.isInitialized) return

        databaseProvider = StandaloneDatabaseProvider(context.applicationContext)

        val cacheDir = File(context.applicationContext.cacheDir, "exo_cache")
        val evictor = LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE)

        simpleCache = SimpleCache(cacheDir, evictor, databaseProvider)

        val upstreamFactory = DefaultDataSource.Factory(context.applicationContext)
        cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    fun getFactory(): DataSource.Factory = cacheDataSourceFactory
}