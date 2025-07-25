package com.example.scrollbooker.ui.profile.components.userInformation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingM

@Composable
fun ProfileBio(bio: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            top = SpacingM,
            start = 50.dp,
            end = 50.dp,
            bottom = 0.dp
        ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = bio,
            textAlign = TextAlign.Center
        )
    }
}