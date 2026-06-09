package com.example.scrollbooker.ui.myBusiness.myEmployees.tabs.employmentRequestsTab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.AppLocaleProvider
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun EmploymentRequestCard(
    employmentRequest: EmploymentRequest,
    onClick: (Int) -> Unit
) {
    val zone = remember { ZoneId.systemDefault() }

    val createdAt = remember(employmentRequest.createdAt) {
        employmentRequest.createdAt
            .withZoneSameInstant(zone)
            .format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", AppLocaleProvider.current()))
    }

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
            Avatar(url = employmentRequest.employee.avatar ?: "")

            Spacer(Modifier.width(SpacingS))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = employmentRequest.employee.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = OnSurfaceBG
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "@${employmentRequest.employee.username}",
                    style = bodyMedium,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { onClick(employmentRequest.id) },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = stringResource(R.string.cancelRequest),
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
                    text = stringResource(R.string.proposedProfession),
                    style = bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = employmentRequest.profession.name,
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
                    text = stringResource(R.string.date),
                    style = bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = createdAt,
                    style = bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = OnSurfaceBG.copy(alpha = 0.8f)
                )
            }
        }
    }
}
