package com.example.scrollbooker.ui.shared.userProducts.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.ui.theme.Divider

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EmployeesList(
    employees: List<UserSocial>,
    onSetEmployee: (Int) -> Unit
) {
    val lazyRowListState = rememberLazyListState()

    var selectedEmployeesIndex by remember { mutableIntStateOf(0) }
    val scrollOffset = with(LocalDensity.current) {
        -BasePadding.roundToPx()
    }

    LaunchedEffect(selectedEmployeesIndex) {
        lazyRowListState.animateScrollToItem(
            index = selectedEmployeesIndex,
            scrollOffset = scrollOffset
        )
    }

    LazyRow(
        state = lazyRowListState,
        modifier = Modifier.padding(vertical = BasePadding)
    ) {
        item { Spacer(Modifier.width(BasePadding)) }

        itemsIndexed(employees) { index, emp ->
            val isSelected = selectedEmployeesIndex == index

            EmployeeTab(
                isSelected = isSelected,
                avatar = emp.avatar,
                fullName = emp.fullName,
                ratingsAverage = emp.ratingsAverage,
                onSelectedEmployee = {
                    selectedEmployeesIndex = index
                    onSetEmployee(emp.id)
                }
            )

            Spacer(Modifier.width(BasePadding))
        }
    }

    HorizontalDivider(color = Divider, thickness = 0.55.dp)
}