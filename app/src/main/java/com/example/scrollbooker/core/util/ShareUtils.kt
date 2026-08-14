package com.example.scrollbooker.core.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.core.extensions.toSlug
import com.example.scrollbooker.entity.social.post.domain.model.Post

val SHARE_BASE_URL = "https://scrollbooker-web.vercel.app"

class ShareChannelReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_SHARE_RESULT = "action_share_result"
        var pendingCallback: ((ShareChannelEnum) -> Unit)? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val chosenComponent = getChosenComponent(intent)
        val packageName = chosenComponent?.packageName.orEmpty()

        val channel = mapPackageToChannel(packageName)
        pendingCallback?.invoke(channel)
        pendingCallback = null
    }

    private fun getChosenComponent(intent: Intent): ComponentName? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT, ComponentName::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Intent.EXTRA_CHOSEN_COMPONENT)
        }
    }

    private fun mapPackageToChannel(packageName: String): ShareChannelEnum {
        return when {
            packageName.contains("whatsapp") -> ShareChannelEnum.WHATSAPP
            packageName.contains("facebook.katana") -> ShareChannelEnum.FACEBOOK
            packageName.contains("instagram") -> ShareChannelEnum.INSTAGRAM
            packageName.contains("orca") -> ShareChannelEnum.MESSENGER
            packageName.contains("mms") || packageName.contains("messaging") -> ShareChannelEnum.SMS
            packageName.contains("gm") || packageName.contains("email") -> ShareChannelEnum.EMAIL
            else -> ShareChannelEnum.OTHER
        }
    }
}

fun sharePost(
    context: Context,
    post: Post,
    onChannelResolved: (ShareChannelEnum) -> Unit
) {
    val postUrl = "$SHARE_BASE_URL/user/${post.user.username}/${post.user.profession.toSlug()}/post/${post.id}"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, buildString {
            if (!post.description.isNullOrBlank()) append("${post.description}\n")
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
        context.getString(R.string.sharePost),
        pendingIntent.intentSender
    )

    context.startActivity(chooser)
}

fun shareUserProfile(
    context: Context,
    userSharedUsername: String,
    userSharedProfession: String,
    userSharedBio: String?,
    onChannelResolved: (ShareChannelEnum) -> Unit
) {
    val postUrl = "$SHARE_BASE_URL/user/${userSharedUsername}/${userSharedProfession.toSlug()}"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, buildString {
            if (!userSharedBio.isNullOrBlank()) append("$userSharedBio\n")
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
        context.getString(R.string.shareProfile),
        pendingIntent.intentSender
    )

    context.startActivity(chooser)
}

fun shareBusinessProfile(
    context: Context,
    businessOwnerUsername: String,
    businessOwnerProfession: String,
    businessDescription: String?,
    onChannelResolved: (ShareChannelEnum) -> Unit
) {
    val postUrl = "$SHARE_BASE_URL/business/${businessOwnerProfession.toSlug()}/${businessOwnerUsername}"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, buildString {
            if (!businessDescription.isNullOrBlank()) append("$businessDescription\n")
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
        context.getString(R.string.shareProfile),
        pendingIntent.intentSender
    )

    context.startActivity(chooser)
}