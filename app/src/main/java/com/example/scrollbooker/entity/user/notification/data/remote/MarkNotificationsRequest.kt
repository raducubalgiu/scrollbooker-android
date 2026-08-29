package com.example.scrollbooker.entity.user.notification.data.remote

import com.google.gson.annotations.SerializedName

data class MarkNotificationsRequest(
    @SerializedName("notification_ids")
    val notificationIds: List<Int>
)