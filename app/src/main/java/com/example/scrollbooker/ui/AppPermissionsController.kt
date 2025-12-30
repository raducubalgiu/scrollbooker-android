package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.paging.PagingData
import com.example.scrollbooker.entity.permission.domain.model.MediaVideo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

data class AppPermissionsController(
    val state: StateFlow<PermissionState>,
    val refreshMedia: () -> Unit,
    val markMediaRequested: () -> Unit,

    val videos: Flow<PagingData<MediaVideo>>,
)

val LocalAppPermissions = staticCompositionLocalOf<AppPermissionsController> {
    error("LocalAppPermissions not provided")
}