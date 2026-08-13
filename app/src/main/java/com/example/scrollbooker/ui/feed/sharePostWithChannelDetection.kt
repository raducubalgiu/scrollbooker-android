package com.example.scrollbooker.ui.feed

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.scrollbooker.core.enums.ShareChannelEnum

fun sharePostWithChannelDetection(
    context: Context,
    postUrl: String,
    postTitle: String?,
    onChannelResolved: (ShareChannelEnum) -> Unit
) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, buildString {
            if (!postTitle.isNullOrBlank()) append("$postTitle\n")
            append(postUrl)
        })
    }

    val receiverIntent = Intent(context, ShareChannelReceiver::class.java).apply {
        action = ShareChannelReceiver.ACTION_SHARE_RESULT
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        System.currentTimeMillis().toInt(),
        receiverIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    ShareChannelReceiver.pendingCallback = onChannelResolved

    val chooser = Intent.createChooser(
        sendIntent,
        "Distribuie postarea",
        pendingIntent.intentSender
    )

    context.startActivity(chooser)
}