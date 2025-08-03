package com.example.scrollbooker.ui.profile.components.profileHeader.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXXL

@Composable
fun ProfileIntentActionsList() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = SpacingXXL)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProfileIntentActionButton(
                icon = painterResource(R.drawable.ic_map_outline),
                title = stringResource(R.string.address)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(10.dp)
                    .background(Color.Gray, shape = RectangleShape)
                    .padding(vertical = BasePadding)
            )

            ProfileIntentActionButton(
                icon = painterResource(R.drawable.ic_email_outline),
                title = stringResource(R.string.email)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(10.dp)
                    .background(Color.Gray, shape = RectangleShape)
                    .padding(vertical = BasePadding)
            )

            ProfileIntentActionButton(
                icon = painterResource(R.drawable.ic_globe_outline),
                title = stringResource(R.string.website)
            )
        }

        Spacer(Modifier.height(SpacingM))
    }
}