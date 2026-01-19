package com.example.scrollbooker.ui.profile.components.userInfo.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

data class IntentActionSpec(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val valueOf: (UserProfile) -> String,
    val onlyBusinessOrEmployee: Boolean
)

data class IntentAction(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val value: String,
    val isBusinessOrEmployee: Boolean
)

val INTENT_ACTION_SPECS = listOf<IntentActionSpec>(
    IntentActionSpec(
        icon = R.drawable.ic_call_outline,
        title = R.string.phone,
        valueOf = {""},
        onlyBusinessOrEmployee = true
    ),
    IntentActionSpec(
        icon = R.drawable.ic_location_outline,
        title = R.string.address,
        valueOf = {""},
        onlyBusinessOrEmployee = true
    ),
    IntentActionSpec(
        icon = R.drawable.ic_email_outline,
        title = R.string.email,
        valueOf = { it.publicEmail.orEmpty() },
        onlyBusinessOrEmployee = true
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.website,
        valueOf = { it.website.orEmpty() },
        onlyBusinessOrEmployee = true
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.instagram,
        valueOf = { it.instagram.orEmpty() },
        onlyBusinessOrEmployee = false
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.youtube,
        valueOf = { it.youtube.orEmpty() },
        onlyBusinessOrEmployee = false
    ),
    IntentActionSpec(
        icon = R.drawable.ic_globe_outline,
        title = R.string.tikTok,
        valueOf = { it.tikTok.orEmpty() },
        onlyBusinessOrEmployee = false
    )
)