package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.block.BlockSlotsSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.detail.MyCalendarAppointmentDetailSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientAction
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient.OwnClientSheetState
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.settings.MyCalendarSettingsSheet
import com.example.scrollbooker.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarSheets(
    sheetState: SheetState,
    controller: MyCalendarSheetController,
    ownClientState: OwnClientSheetState,
    onOwnClientAction: (OwnClientAction) -> Unit,
    blockState: BlockSlotsSheetState,
    onBlockAction: (BlockSlotsAction) -> Unit,
) {
    val current = controller.currentSheet ?: return

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = {
            if (controller.canDismissRequest()) controller.close()
        },
        containerColor = Background,
        dragHandle = {}
    ) {
        when (current) {
            MyCalendarSheet.Settings -> MyCalendarSettingsSheet()

            MyCalendarSheet.Detail -> MyCalendarAppointmentDetailSheet()

            MyCalendarSheet.Block -> BlockSlotsSheet(
                state = blockState,
                onAction = onBlockAction
            )

            MyCalendarSheet.OwnClient -> OwnClientSheet(
                state = ownClientState,
                onAction = onOwnClientAction
            )
        }
    }
}
