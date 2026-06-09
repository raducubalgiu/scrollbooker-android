package com.example.scrollbooker.ui.profile.tabs.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileEmployee(
    employee: Employee,
    onNavigateToEmployeeProfile: (userId: Int, username: String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onNavigateToEmployeeProfile(employee.id, employee.username) }),
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
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = bodyMedium,
                text = "${employee.job} • ${employee.productsCount} ${stringResource(R.string.services)}"
            )
        },
        trailingContent = {
            MainButtonSmall(
                modifier = Modifier.clip(shape = ShapeDefaults.ExtraLarge),
                title = stringResource(R.string.pick),
                onClick = {}
            )
        },
        leadingContent = {
            AvatarWithRating(
                url = employee.avatar ?: "",
                rating = employee.ratingsAverage,
                size = 60.dp,
                onClick = { onNavigateToEmployeeProfile(employee.id, employee.username) }
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = Background
        )
    )
}