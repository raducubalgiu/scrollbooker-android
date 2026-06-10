package com.example.scrollbooker.ui.search.businessProfile.sections.employees
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun BusinessEmployeesSection(
    employees: List<BusinessProfileEmployee>,
    onNavigateToEmployeeProfile: (userId: Int, username: String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = BasePadding)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.team),
            style = headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXXL))

        LazyRow(
            contentPadding = PaddingValues(horizontal = BasePadding)
        ) {
            itemsIndexed(employees) { index, employee ->
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .clickable(
                            onClick = {},
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AvatarWithRating(
                            size = 90.dp,
                            url = "${employee.avatar}",
                            rating = employee.ratingsAverage,
                            onClick = {}
                        )
                        Spacer(Modifier.height(BasePadding))
                        Text(
                            modifier = Modifier.padding(horizontal = BasePadding),
                            style = bodyMedium,
                            textAlign = TextAlign.Center,
                            text = employee.fullName,
                            fontWeight = FontWeight.SemiBold,
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if(index < employees.size - 1 ) {
                    Spacer(Modifier.width(SpacingXXL))
                }
            }
        }
    }
}