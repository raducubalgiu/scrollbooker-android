package com.example.scrollbooker.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.AvatarSizeS
import com.example.scrollbooker.ui.theme.ScrollBookerTheme

@Composable
fun Avatar(url: String, size: Dp = AvatarSizeS ) {
    AsyncImage(
        model = url,
        contentDescription = "User Avatar",
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        placeholder = painterResource(R.drawable.ic_user),
        error = painterResource(R.drawable.ic_user),
        contentScale = ContentScale.Crop
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AvatarPreview() {
    ScrollBookerTheme(dynamicColor = false) {
        Avatar(url = "")
    }
}