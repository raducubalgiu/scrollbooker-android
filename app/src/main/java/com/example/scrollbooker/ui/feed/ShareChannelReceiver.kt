package com.example.scrollbooker.ui.feed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.scrollbooker.core.enums.ShareChannelEnum

class ShareChannelReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SHARE_RESULT = "action_share_result"
        var pendingCallback: ((ShareChannelEnum) -> Unit)? = null
    }

    override fun onReceive(context: Context, intent: Intent) {
        val chosenComponent = intent.getParcelableExtra<android.content.ComponentName>(
            Intent.EXTRA_CHOSEN_COMPONENT
        )
        val packageName = chosenComponent?.packageName.orEmpty()

        val channel = mapPackageToChannel(packageName)
        pendingCallback?.invoke(channel)
        pendingCallback = null
    }

    private fun mapPackageToChannel(packageName: String): ShareChannelEnum {
        return when {
            packageName.contains("whatsapp") -> ShareChannelEnum.WHATSAPP
            packageName.contains("gm") || packageName.contains("email") -> ShareChannelEnum.EMAIL
            else -> ShareChannelEnum.OTHER
        }
    }
}