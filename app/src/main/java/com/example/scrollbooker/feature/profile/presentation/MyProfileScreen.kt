package com.example.scrollbooker.feature.profile.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.feature.profile.presentation.components.ProfileHeader
import com.example.scrollbooker.feature.profile.presentation.components.ProfileLayout
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileActions
import com.example.scrollbooker.feature.profile.presentation.components.myProfile.MyProfileMenuList
import com.example.scrollbooker.ui.theme.titleMedium

data class Schedule(
    val dayOfWeek: String,
    val startTime: String?,
    val endTime: String?
)

val schedules = listOf<Schedule>(
    Schedule(
        dayOfWeek = "Luni",
        startTime = "09:00",
        endTime = "18:00"
    ),
    Schedule(
        dayOfWeek = "Marti",
        startTime = "09:00",
        endTime = "18:00"
    ),
    Schedule(
        dayOfWeek = "Miercuri",
        startTime = "09:00",
        endTime = "18:00"
    ),
    Schedule(
        dayOfWeek = "Luni",
        startTime = "09:00",
        endTime = "18:00"
    ),
    Schedule(
        dayOfWeek = "Joi",
        startTime = "09:00",
        endTime = "18:00"
    ),
    Schedule(
        dayOfWeek = "Sambata",
        startTime = null,
        endTime = null
    ),
    Schedule(
        dayOfWeek = "Duminica",
        startTime = null,
        endTime = null
    ),
)

@Composable
fun MyProfileScreen(
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit
) {
    var showMenuSheet by remember { mutableStateOf(false) }
    var showScheduleSheet by remember { mutableStateOf(false) }
    val user = viewModel.user
    val userId = user?.id
    val username = viewModel.user?.username ?: ""

    BottomSheet(
        onDismiss = { showMenuSheet = false },
        showBottomSheet = showMenuSheet,
        showHeader = false,
    ) {
        MyProfileMenuList(
            onNavigate = {
                onNavigate(it)
                showMenuSheet = false
            }
        )
    }

    BottomSheet(
        onDismiss = { showScheduleSheet = false },
        showBottomSheet = showScheduleSheet,
        showHeader = true,
        headerTitle = "Program"
    ) {
        schedules.forEach { (dayOfWeek, startTime, endTime) ->
            val text = if (startTime.isNullOrBlank()) "Inchis"
            else "$startTime - $endTime"

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = BasePadding,
                        vertical = SpacingM
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    text = dayOfWeek
                )
                Text(text)
            }
        }
    }

    ProfileLayout(
        ratingsCount = user?.counters?.ratingsCount ?: 0,
        followersCount = user?.counters?.followersCount ?: 0,
        followingsCount = user?.counters?.followingsCount ?: 0,
        fullName = user?.fullName ?: "",
        profession = user?.profession ?: "",
        onNavigateCounters = { onNavigate("$it/$userId/$username") },
        onOpenScheduleSheet = { showScheduleSheet = true },
        header = { 
            ProfileHeader(
                username = username,
                onOpenBottomSheet = { showMenuSheet = true }
            ) 
        }
    ) {
        MyProfileActions(
            onNavigate={ onNavigate(MainRoute.EditProfile.route) }
        )
    }
}