package com.example.scrollbooker.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object MediaPermissionHelper {

    fun getMediaPermissions(): Array<String> {
        return when {
            Build.VERSION.SDK_INT >= 34 -> arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )
            Build.VERSION.SDK_INT >= 33 -> arrayOf(
                Manifest.permission.READ_MEDIA_VIDEO
            )
            else -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    fun checkMediaPermissionState(context: Context): MediaPermissionState {
        return when {
            Build.VERSION.SDK_INT >= 34 -> {
                val fullAccess = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VIDEO
                ) == PackageManager.PERMISSION_GRANTED

                val partialAccess = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) == PackageManager.PERMISSION_GRANTED

                when {
                    fullAccess -> MediaPermissionState.GRANTED
                    partialAccess -> MediaPermissionState.PARTIAL
                    else -> MediaPermissionState.DENIED
                }
            }
            Build.VERSION.SDK_INT >= 33 -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    MediaPermissionState.GRANTED
                } else {
                    MediaPermissionState.DENIED
                }
            }
            else -> {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    MediaPermissionState.GRANTED
                } else {
                    MediaPermissionState.DENIED
                }
            }
        }
    }

    fun evaluateMediaPermissionState(
        context: Context,
        grants: Map<String, Boolean>
    ): MediaPermissionState {
        return when {
            Build.VERSION.SDK_INT >= 34 -> {
                val fullGranted = grants[Manifest.permission.READ_MEDIA_VIDEO] == true
                val partialGranted = grants[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == true

                when {
                    fullGranted -> MediaPermissionState.GRANTED
                    partialGranted -> MediaPermissionState.PARTIAL
                    grants.values.any { it == false } -> {
                        val canShowRationale =
                            (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                                Manifest.permission.READ_MEDIA_VIDEO
                            ) ?: false

                        if (canShowRationale) MediaPermissionState.DENIED
                        else MediaPermissionState.DENIED_PERMANENTLY
                    }
                    else -> MediaPermissionState.DENIED
                }
            }
            Build.VERSION.SDK_INT >= 33 -> {
                val granted = grants[Manifest.permission.READ_MEDIA_VIDEO] == true

                if (granted) {
                    MediaPermissionState.GRANTED
                } else {
                    val canShowRationale =
                        (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_MEDIA_VIDEO
                        ) ?: false

                    if (canShowRationale) MediaPermissionState.DENIED
                    else MediaPermissionState.DENIED_PERMANENTLY
                }
            }
            else -> {
                val granted = grants[Manifest.permission.READ_EXTERNAL_STORAGE] == true

                if (granted) {
                    MediaPermissionState.GRANTED
                } else {
                    val canShowRationale =
                        (context as? android.app.Activity)?.shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) ?: false

                    if (canShowRationale) MediaPermissionState.DENIED
                    else MediaPermissionState.DENIED_PERMANENTLY
                }
            }
        }
    }
}