package com.example.scrollbooker.entity.permission.data.remote

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosPagingSource(
    private val context: Context,
    private val pageSize: Int
) : PagingSource<Int, MediaVideo>() {

    override fun getRefreshKey(state: PagingState<Int, MediaVideo>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(pageSize) ?: page.nextKey?.minus(pageSize)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaVideo> {
        val offset = params.key ?: 0
        val limit = params.loadSize.coerceAtMost(pageSize)

        return try {
            val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE
            )

            val sortColumns = arrayOf(
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media._ID
            )
            val sortDirection = intArrayOf(
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING,
                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
            )

            val items = withContext(Dispatchers.IO) {
                val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val args = Bundle().apply {
                        putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                        putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                        putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, sortColumns)
                        putIntArray(ContentResolver.QUERY_ARG_SORT_DIRECTION, sortDirection)
                    }
                    context.contentResolver.query(collection, projection, args, null)
                } else {
                    // Fallback vechi (dacă îți pasă)
                    val sortOrder =
                        "${MediaStore.Video.Media.DATE_ADDED} DESC, ${MediaStore.Video.Media._ID} DESC " +
                                "LIMIT $limit OFFSET $offset"
                    context.contentResolver.query(collection, projection, null, null, sortOrder)
                }

                cursor?.use { c ->
                    val idCol = c.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    val nameCol = c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    val durCol = c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                    val dateCol = c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)
                    val sizeCol = c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

                    val list = ArrayList<MediaVideo>(c.count)
                    while (c.moveToNext()) {
                        val id = c.getLong(idCol)
                        val uri = ContentUris.withAppendedId(collection, id)
                        list.add(
                            MediaVideo(
                                id = id,
                                uri = uri,
                                displayName = c.getString(nameCol),
                                durationMs = c.getLong(durCol),
                                dateAddedSec = c.getLong(dateCol),
                                sizeBytes = c.getLong(sizeCol)
                            )
                        )
                    }
                    list
                } ?: emptyList()
            }

            val prevKey = if (offset == 0) null else (offset - limit).coerceAtLeast(0)
            val nextKey = if (items.isEmpty()) null else offset + items.size

            LoadResult.Page(
                data = items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }
}