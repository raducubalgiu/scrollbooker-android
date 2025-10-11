package com.example.scrollbooker.components.customized

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelSmall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

enum class MediaFilter { ALL, PHOTOS, VIDEOS }
enum class MediaType { PHOTO, VIDEO }

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val mimeType: String?,
    val dateAddedSeconds: Long,
    val durationMs: Long?,
    val type: MediaType
)

@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaLibraryBottomSheet(
    sheetState: SheetState,
    initialFilter: MediaFilter = MediaFilter.ALL,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var filter by remember { mutableStateOf(initialFilter) }
    var media by remember(filter) { mutableStateOf<List<MediaItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    fun queryMedia(
        context: Context,
        filter: MediaFilter
    ): List<MediaItem> {
        val collection = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.Video.VideoColumns.DURATION // pentru imagini e null
        )

        val (selection, args) = when (filter) {
            MediaFilter.ALL -> {
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR " +
                        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                )
            }
            MediaFilter.PHOTOS -> {
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
                )
            }
            MediaFilter.VIDEOS -> {
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" to arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                )
            }
        }

        val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"

        val items = mutableListOf<MediaItem>()
        context.contentResolver.query(
            collection, projection, selection, args, sortOrder
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val typeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
            val mimeCol = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)
            val durCol = cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val mediaTypeInt = cursor.getInt(typeCol)
                val mime = cursor.getString(mimeCol)
                val dateAdded = cursor.getLong(dateCol)
                val dur = if (mediaTypeInt == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                    cursor.getLong(durCol) else null

                val contentUri = when (mediaTypeInt) {
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ->
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ->
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    else -> null
                } ?: continue

                val type = if (dur != null) MediaType.VIDEO else MediaType.PHOTO
                items += MediaItem(
                    id = id,
                    uri = contentUri,
                    mimeType = mime,
                    dateAddedSeconds = dateAdded,
                    durationMs = dur,
                    type = type
                )
            }
        }

        return items
    }

    LaunchedEffect(filter) {
        loading = true
        media = withContext(Dispatchers.IO) {
            queryMedia(context, filter)
        }
        loading = false
    }

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
    ) {
        val height = maxHeight

        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = sheetState,
            shape = ShapeDefaults.Medium,
            containerColor = Background,
            dragHandle = {},
        ) {
            Column(Modifier.height(height)) {
                SheetHeader(
                    title = "",
                    onClose = onClose
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    reverseLayout = true
                ) {
                    items(media, key = { it.id }) { item ->
                        Box(modifier = Modifier
                            .aspectRatio(9f / 12f)
                            .clip(shape = ShapeDefaults.Medium)
                            .background(SurfaceBG)
                            .clickable(onClick = {})
                        ) {
                            AsyncImage(
                                model = if(item.type == MediaType.VIDEO) {
                                    ImageRequest.Builder(LocalContext.current)
                                        .data(item.uri)
                                        .videoFrameMillis(500)
                                        .decoderFactory { result, options, _ ->
                                            VideoFrameDecoder(
                                                result.source,
                                                options
                                            )
                                        }
                                        .crossfade(true)
                                        .build()
                                } else item.uri,
                                contentDescription = "Post Grid",
                                contentScale = ContentScale.Crop,
                                onError = { Timber.tag("Post Grid Error").e("ERROR: ${it.result.throwable.message}") },
                                modifier = Modifier.matchParentSize()
                            )

                            Box(modifier = Modifier
                                .matchParentSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.2f),
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.4f)
                                        )
                                    )
                                )
                            )

                            if(item.type == MediaType.VIDEO && item.durationMs != null) {
                                Text(
                                    text = formatDuration(item.durationMs),
                                    style = labelSmall,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(6.dp)
                                        .background(color = BackgroundDark.copy(alpha = 0.3f), shape = ShapeDefaults.Small)
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val totalSec = durationMs / 1000
    val m = totalSec / 60
    val s = totalSec % 60
    return "%d:%02d".format(m, s)
}