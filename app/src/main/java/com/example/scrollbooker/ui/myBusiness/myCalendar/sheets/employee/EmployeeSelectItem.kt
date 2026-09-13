package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.titleMedium

private val AvailableDotColor = Color(0xFF4CAF50)
private val FullyBookedDotColor = Color(0xFF9E9E9E)

@Composable
fun EmployeeSelectItem(
    employee: Employee,
    selectedEmployeeId: Int?,
    employeesAvailability: Map<Int, Boolean>,
    onAction: (EmployeeSheetAction) -> Unit
) {
    val availabilityMessage = when (employeesAvailability[employee.id]) {
        true -> stringResource(R.string.employeeHasAvailability)
        false -> stringResource(R.string.employeeFullyBooked)
        null -> null
    }

    InputRadio(
        paddingHorizontal = 0.dp,
        selected = employee.id == selectedEmployeeId,
        headLine = employee.fullName,
        headLineStyle = titleMedium.copy(fontWeight = FontWeight.SemiBold),
        subHeadline = availabilityMessage ?: employee.job,
        leadingIcon = {
            Box(modifier = Modifier.padding(end = SpacingS)) {
                Avatar(
                    url = employee.avatar ?: "",
                    size = 40.dp
                )

                val hasAvailability = employeesAvailability[employee.id]
                if (hasAvailability != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Background)
                            .border(2.dp, Background, CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (hasAvailability) AvailableDotColor else FullyBookedDotColor
                                )
                        )
                    }
                }
            }
        },
        onSelect = { onAction(EmployeeSheetAction.Select(employee.id)) }
    )
}