package com.example.scrollbooker.screens.profile.myBusiness.myEmployees.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.shared.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EmployeeCard(
    employee: Employee,
    onNavigate: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBG)
        .padding(SpacingM)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(url = "")
            Spacer(Modifier.width(SpacingM))
            Column {
                Text(
                    text = employee.username,
                    style = titleMedium
                )
                Text(
                    text = "Frizer",
                    style = bodyLarge,
                    color = OnSurfaceBG
                )
            }
        }
        Spacer(Modifier.height(SpacingXXL))
        Text(
            text = "Data angajarii: ${employee.hireDate}",
            style = bodyLarge,
        )
        Spacer(Modifier.height(SpacingXXL))
        MainButton(
            colors = ButtonColors(
                containerColor = Error,
                contentColor = OnError,
                disabledContainerColor = SurfaceBG,
                disabledContentColor = Divider
            ),
            title = "Demite",
            onClick = {
                onNavigate("${MainRoute.EmployeesDismissal.route}/1")
            }
        )
    }
}