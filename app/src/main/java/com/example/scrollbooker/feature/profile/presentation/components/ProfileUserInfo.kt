package com.example.scrollbooker.feature.profile.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun ProfileUserInfo(
    fullName: String,
    profession: String,
    onOpenScheduleSheet: () -> Unit
) {
    Column(modifier = Modifier
        .padding(horizontal = SpacingXL)
        .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, Divider, CircleShape)
                )
                Box(modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(Color.Green)
                    .border(3.dp, Color.White, CircleShape)
                )
            }
            Spacer(Modifier.width(BasePadding))
            Column {
                Text(
                    text = fullName,
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.width(10.dp))
                Spacer(Modifier.height(SpacingXXS))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = profession,
                        style = titleSmall,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text = "4.5",
                        style = titleMedium,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                }
                Spacer(Modifier.height(SpacingS))
                Row(modifier = Modifier
                        .clickable(onClick = onOpenScheduleSheet),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = OnSurfaceBG
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = "Inchide la 19:00",
                        style = bodyMedium,
                        color = OnSurfaceBG
                    )
                    Spacer(Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = OnSurfaceBG
                    )
                }
            }
        }
    }
}