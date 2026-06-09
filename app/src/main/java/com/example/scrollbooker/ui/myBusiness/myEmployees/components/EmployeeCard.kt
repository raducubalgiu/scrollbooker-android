package com.example.scrollbooker.ui.myBusiness.myEmployees.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import com.example.scrollbooker.R

@Composable
fun EmployeeCard(
    employee: Employee,
    onNavigateToDismissalScreen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = SpacingXL,
                end = SpacingXL,
                bottom = BasePadding
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
                size = 60.dp,
                onClick = {}
            )

            Spacer(Modifier.width(SpacingM))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = employee.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = OnSurfaceBG
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = employee.job,
                    style = bodyMedium,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = onNavigateToDismissalScreen,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PersonOff,
                    contentDescription = stringResource(R.string.dismiss),
                    tint = Error.copy(alpha = 0.85f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(SpacingM))
        HorizontalDivider(color = Divider.copy(alpha = 0.5f), thickness = 0.5.dp)
        Spacer(Modifier.height(SpacingM))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.hireDate),
                    style = bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = employee.hireDate,
                    style = bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceBG
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.managedProducts),
                    style = bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = employee.productsCount.toString(),
                    style = bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = OnSurfaceBG
                )
            }
        }
    }
}
