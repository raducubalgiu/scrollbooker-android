package com.example.scrollbooker.ui.profile.tabs.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.profile.components.profileHeader.components.UserScheduleSheet
import com.example.scrollbooker.ui.theme.headlineMedium

@Composable
fun ProfileInfoTab(paddingTop: Dp) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingTop)
        .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.about),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")

        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.schedule),
            style = headlineMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(SpacingXL))

        UserScheduleSheet()
    }
}