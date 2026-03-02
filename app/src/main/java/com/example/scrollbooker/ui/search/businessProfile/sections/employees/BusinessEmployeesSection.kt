package com.example.scrollbooker.ui.search.businessProfile.sections.employees
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee
import com.example.scrollbooker.ui.theme.headlineSmall

@Composable
fun BusinessEmployeesSection(
    employees: List<BusinessProfileEmployee>,
    onNavigateToEmployeeProfile: (Int) -> Unit
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = BasePadding)
        ) {
            itemsIndexed(employees) { index, employee ->
                BusinessEmployeeItem(
                    fullName = employee.fullName,
                    profession = employee.profession,
                    avatar = employee.avatar ?: "",
                    ratingsAverage = employee.ratingsAverage,
                    onNavigateToUserProfile = {
                        onNavigateToEmployeeProfile(employee.id)
                    }
                )

                if(index < employees.size - 1 ) {
                    Spacer(Modifier.width(SpacingXL))
                }
            }
        }
    }
}