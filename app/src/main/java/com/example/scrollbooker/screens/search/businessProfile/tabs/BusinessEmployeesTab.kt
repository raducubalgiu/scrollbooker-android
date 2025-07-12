package com.example.scrollbooker.screens.search.businessProfile.tabs

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
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

data class BusinessEmp(
    val fullName: String,
    val avatar: String,
    val rating: String
)

@Composable
fun BusinessEmployeesTab() {
    val employees = listOf(
        BusinessEmp(avatar = "https://media.scrollbooker.ro/avatar-male-9.jpeg", fullName ="Cristian Ionel", rating= "4.9"),
        BusinessEmp(avatar = "https://media.scrollbooker.ro/avatar-male-10.jpg", fullName ="Radu Dan", rating = "4.3"),
        BusinessEmp(avatar = "https://media.scrollbooker.ro/avatar-male-11.jpeg", fullName ="Laur Oprea", rating = "4.2"),
        BusinessEmp(avatar = "https://media.scrollbooker.ro/avatar-male-12.jpg", fullName ="Mihai Gandac", rating = "4.9"),
        BusinessEmp(avatar = "https://media.scrollbooker.ro/avatar-male-14.jpeg", fullName ="Gigi Corsicanu", rating = "3.2"),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.team),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        LazyRow {
            item { Spacer(Modifier.width(BasePadding)) }

            items(employees) { employee ->
                Column(
                    modifier = Modifier.padding(end = BasePadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AvatarWithRating(
                        url = employee.avatar,
                        rating = employee.rating,
                        size = 100.dp
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
                        title = "Profil",
                        onClick = {}
                    )
                }
                Spacer(Modifier.width(SpacingXL))
            }
        }
    }
}