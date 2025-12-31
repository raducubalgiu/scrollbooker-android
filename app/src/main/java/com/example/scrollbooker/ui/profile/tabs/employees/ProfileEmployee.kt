package com.example.scrollbooker.ui.profile.tabs.employees

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileEmployee(
    employee: Employee,
    onNavigateToEmployeeProfile: (Int) -> Unit
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        headlineContent = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = SpacingXXS),
                style = titleMedium,
                color = OnBackground,
                text = employee.fullName
            )
        },
        supportingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = bodyMedium,
                    text = employee.job
                )
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "\u2022",
                    color = Color.Gray
                )
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = bodyMedium,
                    color = Primary,
                    fontWeight = FontWeight.Bold,
                    text = "${employee.productsCount} ${stringResource(R.string.services)}"
                )
            }
        },
        trailingContent = {
            MainButtonOutlined(
                title = stringResource(R.string.profile),
                onClick = { onNavigateToEmployeeProfile(employee.id) }
            )
        },
        leadingContent = {
            AvatarWithRating(
                url = employee.avatar ?: "",
                rating = employee.ratingsAverage,
                size = 60.dp,
                onClick = { onNavigateToEmployeeProfile(employee.id) }
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )
}