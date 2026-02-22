package com.example.scrollbooker.ui.profile.components.userInfo.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

enum class IntentActionTypeEnum {
    PHONE,
    ADDRESS,
    EMAIL,
    WEBSITE,
    INSTAGRAM,
    YOUTUBE,
    TIKTOK
}

data class IntentActionSpec(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val valueOf: (UserProfile) -> String,
    val onlyBusinessOrEmployee: Boolean,
    val actionType: IntentActionTypeEnum
)

data class IntentAction(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val value: String,
    val isBusinessOrEmployee: Boolean,
    val actionType: IntentActionTypeEnum
)

val INTENT_ACTION_SPECS = listOf<IntentActionSpec>(
    IntentActionSpec(
        icon = R.drawable.ic_call_outline,
        title = R.string.phone,
        valueOf = {""},
        onlyBusinessOrEmployee = true,
        actionType = IntentActionTypeEnum.PHONE
    ),
    IntentActionSpec(
        icon = R.drawable.ic_location_outline,
        title = R.string.address,
        valueOf = {""},
        onlyBusinessOrEmployee = true,
        actionType = IntentActionTypeEnum.ADDRESS
    ),
    IntentActionSpec(
        icon = R.drawable.ic_email_outline,
        title = R.string.email,
        valueOf = { it.publicEmail.orEmpty() },
        onlyBusinessOrEmployee = true,
        actionType = IntentActionTypeEnum.EMAIL
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.website,
        valueOf = { it.website.orEmpty() },
        onlyBusinessOrEmployee = true,
        actionType = IntentActionTypeEnum.WEBSITE
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.instagram,
        valueOf = { it.instagram.orEmpty() },
        onlyBusinessOrEmployee = false,
        actionType = IntentActionTypeEnum.INSTAGRAM
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.youtube,
        valueOf = { it.youtube.orEmpty() },
        onlyBusinessOrEmployee = false,
        actionType = IntentActionTypeEnum.YOUTUBE
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.tikTok,
        valueOf = { it.tikTok.orEmpty() },
        onlyBusinessOrEmployee = false,
        actionType = IntentActionTypeEnum.TIKTOK
    )
)