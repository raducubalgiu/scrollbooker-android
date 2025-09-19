package com.example.scrollbooker.ui.modules.posts.components.postOverlay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostOverlayButton(
    onClick: () -> Unit,
    title: String
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = ShapeDefaults.Medium,
        contentPadding = PaddingValues(
            vertical = 11.dp,
            horizontal = BasePadding
        ),
        colors = ButtonDefaults.buttonColors(
            contentColor = OnPrimary,
            containerColor = Primary.copy(0.9f)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.ic_calendar_outline),
                contentDescription = null,
                tint = OnPrimary
            )

            Spacer(Modifier.width(BasePadding))

            Text(
                style = bodyMedium,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                text = title
            )

            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = OnPrimary
            )
        }
    }
}