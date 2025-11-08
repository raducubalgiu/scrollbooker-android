package com.example.scrollbooker.ui.myBusiness.myEmploymentRequests.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EmploymentRequestCard(
    employmentRequest: EmploymentRequest,
    onClick: (Int) -> Unit
) {
    val zone = ZoneId.systemDefault()
    val createdAt = employmentRequest.createdAt.withZoneSameInstant(zone)
            .format(DateTimeFormatter.ofPattern("dd MMMM YY HH:mm", Locale("ro")))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = SpacingXL,
                end = SpacingXL,
                bottom = BasePadding
            )
            .shadow(
                elevation = 2.dp,
                shape = ShapeDefaults.Medium,
                clip = false
            )
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .padding(SpacingM)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Avatar(url = employmentRequest.employee.avatar ?: "")
            Spacer(Modifier.width(SpacingS))

            Column {
                Text(
                    text = employmentRequest.employee.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "@${employmentRequest.employee.username}",
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        Text(
            modifier = Modifier.padding(bottom = SpacingS),
            text = "${stringResource(R.string.proposedProfession)}: ${employmentRequest.profession.name}"
        )

        Text(text = "${stringResource(R.string.date)}: $createdAt",)

        Spacer(Modifier.height(BasePadding))

        MainButtonOutlined(
            modifier = Modifier.fillMaxWidth(),
            icon = painterResource(R.drawable.ic_delete_outline),
            iconColor = Error,
            onClick = { onClick(employmentRequest.id) },
            title = stringResource(R.string.cancelRequest)
        )
    }
}