package com.example.scrollbooker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun DeletableItem(
    label: String,
    isSelected: Boolean,
    onDelete: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerColor = if(isSelected) Error.copy(0.2f) else SurfaceBG

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = SpacingS)
            .clip(ShapeDefaults.ExtraSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = ripple(),
                    onClick = {
                        onDelete(label)
                    }
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = BasePadding,
                    vertical = SpacingM
                ),
                color = OnSurfaceBG,
                fontWeight = FontWeight.SemiBold,
                text = label
            )

            Icon(
                modifier = Modifier.padding(end = BasePadding),
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Error
            )
        }
    }
}