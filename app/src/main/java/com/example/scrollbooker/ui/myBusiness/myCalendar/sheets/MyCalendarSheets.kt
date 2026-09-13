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
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee.EmployeeSelectSheet
import com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee.EmployeeSheetAction
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarSheets(
    sheetState: SheetState,
    controller: MyCalendarSheetController,
    blockState: BlockSlotsSheetState,
    onBlockAction: (BlockSlotsAction) -> Unit,
    employees: FeatureState<List<Employee>>,
    selectedEmployeeId: Int?,
    employeesAvailability: Map<Int, Boolean>,
    onEmployeeAction: (EmployeeSheetAction) -> Unit,
) {
    val current = controller.currentSheet ?: return

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = { controller.close() },
        containerColor = Background,
        dragHandle = {}
    ) {
        when (current) {
            MyCalendarSheet.Block -> BlockSlotsSheet(
                state = blockState,
                onAction = onBlockAction
            )

            MyCalendarSheet.Employee -> EmployeeSelectSheet(
                employees = employees,
                selectedEmployeeId = selectedEmployeeId,
                employeesAvailability = employeesAvailability,
                onAction = onEmployeeAction
            )
        }
    }
}
