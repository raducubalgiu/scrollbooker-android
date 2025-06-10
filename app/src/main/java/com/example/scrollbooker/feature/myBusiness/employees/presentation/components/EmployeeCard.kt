package com.example.scrollbooker.feature.myBusiness.employees.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.Avatar
import com.example.scrollbooker.components.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.feature.myBusiness.employees.domain.model.Employee
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EmployeeCard(
    employee: Employee
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
        .padding(SpacingS)
    ) {
        Row {
            Avatar(url = "")
            Column {
                Text(
                    text = employee.username,
                    style = titleMedium
                )
                Text(
                    text = "Frizer",
                    style = bodyLarge,
                )
            }
        }
        Spacer(Modifier.height(BasePadding))
        MainButton(
            title = "Demite",
            onClick = {}
        )
    }
}