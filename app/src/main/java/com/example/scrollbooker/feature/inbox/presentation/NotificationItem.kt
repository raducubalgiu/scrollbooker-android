package com.example.scrollbooker.feature.inbox.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Avatar
import com.example.scrollbooker.core.util.Dimens.AvatarSizeM
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun NotificationItem(
    url: String,
    fullName: String,
    type: String = "follow",
    isFollow: Boolean = false
) {
    val message = when(type) {
        "follow" -> stringResource(id = R.string.startedFollowingYou)
        "like" -> stringResource(id = R.string.likedYourPost)
        else -> ""
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues = PaddingValues(
            bottom = BasePadding,
            start = BasePadding,
            end = BasePadding
        )),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(url = url, size = AvatarSizeM)
            Spacer(Modifier.width(SpacingM))
            Column(Modifier.weight(1f)) {
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    text = fullName
                )
                Text(
                    color = MaterialTheme.colorScheme.onSurface,
                    text = message
                )
            }
        }
        Spacer(Modifier.width(SpacingM))
        if(type == "follow") {
            Button(
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    bottom = 0.dp,
                    top = 0.dp
                ),
                onClick = {},
            ) {
                Text(
                    modifier =
                        Modifier.padding(all = 0.dp),
                    text = if(isFollow) stringResource(id = R.string.following)
                                else stringResource(id = R.string.follow)
                )
            }
        }
    }
}