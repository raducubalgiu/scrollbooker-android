package com.example.scrollbooker.ui.search.businessProfile.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium

data class BusinessEmp(
    val fullName: String,
    val avatar: String,
    val rating: Float
)

@Composable
fun BusinessEmployeesTab(
    employees: List<BusinessProfileEmployee>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(
                horizontal = BasePadding,
                vertical = SpacingXL
            ),
            text = stringResource(R.string.team),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        LazyRow {
            item { Spacer(Modifier.width(BasePadding)) }

            items(employees) { employee ->
                Column(
                    modifier = Modifier.padding(end = BasePadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarWithRating(
                        url = employee.avatar ?: "",
                        rating = employee.ratingsAverage,
                        size = 90.dp,
                        onClick = {}
                    )
                    Spacer(Modifier.height(BasePadding))
                    Text(
                        text = employee.fullName,
                        fontSize = 18.sp,
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(BasePadding))

                    MainButtonOutlined(
                        title = stringResource(R.string.profile),
                        onClick = {}
                    )
                }
                Spacer(Modifier.width(SpacingXL))
            }
        }
    }
}