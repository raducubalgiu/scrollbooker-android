package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun NotFoundPosts() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                tint = Color(0xFFE0E0E0),
                painter = painterResource(R.drawable.ic_video_solid),
                contentDescription = null,
            )
            Spacer(Modifier.height(BasePadding))
            Text(
                style = bodyLarge,
                text = "Nu au fost gasite postari video",
                color = Color(0xFFE0E0E0),
                textAlign = TextAlign.Center
            )
        }
    }
}