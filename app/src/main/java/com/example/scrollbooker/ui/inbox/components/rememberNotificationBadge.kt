package com.example.scrollbooker.ui.inbox.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.core.enums.NotificationTypeEnum

@Composable
fun rememberNotificationBadge(type: NotificationTypeEnum): NotificationBadgeConfig? {
    return remember(type) {
        val iconStylesColor = Color.White
        when (type) {
            NotificationTypeEnum.LIKE_POST -> NotificationBadgeConfig(
                icon = Icons.Filled.Favorite,
                containerColor = Color(0xFFFF1744) // Roșu
            )
            NotificationTypeEnum.COMMENT_POST -> NotificationBadgeConfig(
                icon = Icons.Filled.ChatBubble,
                containerColor = Color(0xFF00B0FF) // Albastru deschis
            )
            NotificationTypeEnum.REPOST -> NotificationBadgeConfig(
                icon = Icons.Filled.Repeat,
                containerColor = Color(0xFF00E676) // Verde
            )
            NotificationTypeEnum.MENTION_POST -> NotificationBadgeConfig(
                icon = Icons.Filled.AlternateEmail,
                containerColor = Color(0xFF9C27B0) // Violet
            )
            NotificationTypeEnum.APPOINTMENT_BOOKED,
            NotificationTypeEnum.APPOINTMENT_RESCHEDULED,
            NotificationTypeEnum.APPOINTMENT_REMINDER -> NotificationBadgeConfig(
                icon = Icons.Filled.CalendarMonth,
                containerColor = Color(0xFF2979FF) // Albastru
            )
            NotificationTypeEnum.APPOINTMENT_CANCELED -> NotificationBadgeConfig(
                icon = Icons.Filled.CalendarMonth,
                containerColor = Color(0xFFFF1744) // Roșu la anulare
            )
            NotificationTypeEnum.EMPLOYMENT_REQUEST,
            NotificationTypeEnum.EMPLOYMENT_REQUEST_ACCEPTED -> NotificationBadgeConfig(
                icon = Icons.Filled.Work,
                containerColor = Color(0xFFFF9100) // Portocaliu
            )
            NotificationTypeEnum.EMPLOYMENT_REQUEST_DENIED -> NotificationBadgeConfig(
                icon = Icons.Filled.Work,
                containerColor = Color(0xFFFF1744) // Roșu
            )
            NotificationTypeEnum.BUSINESS_VALIDATION -> NotificationBadgeConfig(
                icon = Icons.Filled.Verified,
                containerColor = Color(0xFF4CAF50) // Verde validare
            )
            else -> null
        }
    }
}