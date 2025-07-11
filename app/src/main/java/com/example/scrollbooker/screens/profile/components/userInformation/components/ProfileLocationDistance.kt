package com.example.scrollbooker.screens.profile.components.userInformation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R

@Composable
fun ProfileLocationDistance(distance: Float) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location_outline),
            contentDescription = null,
            tint = Color.Gray
        )
        Spacer(Modifier.width(4.dp))
        Text(text =
            buildAnnotatedString {
                append("${stringResource(R.string.at)} ")
                withStyle(SpanStyle(
                    fontWeight = FontWeight.Bold
                )) {
                    append("${distance}km")
                }
                append(" ${stringResource(R.string.fromYou)}")
            }
        )
    }
}