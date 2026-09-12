package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.employee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EmployeeSelectSheet(
    employees: FeatureState<List<Employee>>,
    selectedEmployeeId: Int?,
    onAction: (EmployeeSheetAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.6f)
    ) {
        SheetHeader(
            modifier = Modifier.padding(horizontal = BasePadding),
            title = stringResource(R.string.employee),
            onClose = { onAction(EmployeeSheetAction.Close) }
        )

        Spacer(Modifier.height(BasePadding))

        Box(Modifier.fillMaxWidth()) {
            when (val state = employees) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    val list = state.data

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = BasePadding)
                    ) {
                        itemsIndexed(list, key = { _, employee -> employee.id }) { index, employee ->
                            InputRadio(
                                paddingHorizontal = 0.dp,
                                selected = employee.id == selectedEmployeeId,
                                headLine = employee.fullName,
                                headLineStyle = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                subHeadline = employee.job,
                                leadingIcon = {
                                    Avatar(
                                        modifier = Modifier.padding(end = SpacingS),
                                        url = employee.avatar ?: "",
                                        size = 40.dp
                                    )
                                },
                                onSelect = { onAction(EmployeeSheetAction.Select(employee.id)) }
                            )

                            if (index < list.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = BasePadding),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
