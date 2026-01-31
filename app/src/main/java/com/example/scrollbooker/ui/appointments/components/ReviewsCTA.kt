package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ReviewCTA(
    modifier: Modifier = Modifier,
    onRatingClick: (Int) -> Unit,
) {
    Column(modifier = modifier
        .fillMaxWidth()
        .clip(shape = ShapeDefaults.Medium)
        .background(SurfaceBG)
        .padding(horizontal = BasePadding, vertical = SpacingXL),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.clickOnRatingToEvaluate),
            style = titleMedium,
            color = Color.Gray
        )

        Spacer(Modifier.height(SpacingXL))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) {
                val rating = it + 1

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { onRatingClick(rating) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(37.5.dp),
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}