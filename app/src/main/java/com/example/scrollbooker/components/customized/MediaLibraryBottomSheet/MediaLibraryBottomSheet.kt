package com.example.scrollbooker.components.customized.MediaLibraryBottomSheet

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class MediaFilter { ALL, PHOTOS, VIDEOS }
enum class MediaType { PHOTO, VIDEO }

data class MediaFile(
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
    filter: MediaFilter = MediaFilter.VIDEOS,
    sheetState: SheetState,
    onSelect: (MediaFile) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var media by remember(filter) { mutableStateOf<List<MediaFile>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

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

                if(!loading) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        reverseLayout = true
                    ) {
                        items(media, key = { it.id }) { item ->
                            MediaLibraryGridItem(
                                item = item,
                                onSelect = onSelect
                            )
                        }
                    }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}