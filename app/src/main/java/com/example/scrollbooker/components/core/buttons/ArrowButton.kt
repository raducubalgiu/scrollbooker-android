package com.example.scrollbooker.components.core.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.R

@Composable
fun ArrowButton(
    title: String,
    onClick: () -> Unit,
    isFiltered: Boolean = false,
    onDeleteFilter: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(start = BasePadding)
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
    ) {
        Row(modifier = Modifier.padding(
            vertical = SpacingS,
            horizontal = SpacingM
        ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(SpacingM))

            if(isFiltered) {
                Box(Modifier.clickable { onDeleteFilter?.invoke() }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_circle_solid),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
    }
}