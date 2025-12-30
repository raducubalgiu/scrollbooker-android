package com.example.scrollbooker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun AppPermissionsProvider(
    permissionViewModel: PermissionViewModel,
    content: @Composable () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    // IMPORTANT: ca să nu prinzi lambda vechi în observer
    val refreshMedia = rememberUpdatedState(newValue = {
        permissionViewModel.refreshMedia(force = true) }
    )

    // (opțional) refresh inițial, dacă vrei imediat la start
    LaunchedEffect(Unit) {
        permissionViewModel.refreshMedia(force = true)
    }

    // Refresh de fiecare dată când revii în app (Settings -> back)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                refreshMedia.value.invoke()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val controller = remember(permissionViewModel) {
        AppPermissionsController(
            state = permissionViewModel.mediaState,
            refreshMedia = permissionViewModel::refreshMedia,
            markMediaRequested = permissionViewModel::markMediaPermissionRequested,
            videos = permissionViewModel.videos
        )
    }

    CompositionLocalProvider(LocalAppPermissions provides controller) {
        content()
    }
}