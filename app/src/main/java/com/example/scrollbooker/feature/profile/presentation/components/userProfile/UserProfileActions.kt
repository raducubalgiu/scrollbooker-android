package com.example.scrollbooker.feature.profile.presentation.components.userProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.feature.profile.presentation.components.ProfileActionButton

@Composable
fun UserProfileActions() {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            onClick = {}
        ) {
            Text(
                text = stringResource(R.string.follow)
            )
        }
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(5f),
            onClick = {}
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Green)
                        .height(10.dp)
                        .width(10.dp)
                )
                Spacer(Modifier.width(SpacingS))
                Text("5 locuri libere")
            }
        }
        Spacer(Modifier.width(SpacingS))
        ProfileActionButton(
            modifier = Modifier.weight(1.5f),
            onClick = {}
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}