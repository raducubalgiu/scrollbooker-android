package com.example.scrollbooker.ui.profile.components.sheets
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.SchedulesSection
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

enum class WorkScheduleStatus {
    CLOSED, SHORT, FULL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScheduleSheet(
    sheetState: SheetState,
    layoutViewModel: ProfileLayoutViewModel,
) {
    val scope = rememberCoroutineScope()
    val schedules by layoutViewModel.schedules.collectAsStateWithLifecycle()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { scope.launch { sheetState.hide() } },
        containerColor = Background,
        dragHandle = {}
    ) {
        SheetHeader(
            title = stringResource(R.string.scheduleShort),
            onClose = { scope.launch { sheetState.hide() } }
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(SpacingXL)
        ) {
            when(val state = schedules) {
                is FeatureState.Loading -> ScheduleShimmer()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    SchedulesSection(schedules = state.data)

                    Spacer(Modifier.padding(bottom = BasePadding))
                }
            }
        }
    }
}