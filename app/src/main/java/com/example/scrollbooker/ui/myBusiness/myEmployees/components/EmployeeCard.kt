package com.example.scrollbooker.ui.myBusiness.myEmployees.components
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
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun EmployeeCard(
    employee: Employee,
    onNavigateToDismissalScreen: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = BasePadding)
        .shadow(
            elevation = 2.dp,
            shape = ShapeDefaults.Medium,
            clip = false
        )
        .clip(shape = ShapeDefaults.Medium)
        .background(SurfaceBG)
        .padding(SpacingM)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarWithRating(
                url = employee.avatar ?: "",
                rating = employee.ratingsAverage,
                onClick = {}
            )

            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = employee.fullName,
                    style = titleMedium
                )
                Text(
                    text = employee.job,
                    style = bodyLarge,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(SpacingXXL))

        Text(
            text = "${stringResource(R.string.hireDate)}: ${employee.hireDate}",
            style = bodyLarge,
        )

        Spacer(Modifier.height(BasePadding))

        Text(
            text = "${stringResource(R.string.managedProducts)}: ${employee.productsCount}",
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
            title = stringResource(R.string.dismiss),
            onClick = onNavigateToDismissalScreen
        )
    }
}