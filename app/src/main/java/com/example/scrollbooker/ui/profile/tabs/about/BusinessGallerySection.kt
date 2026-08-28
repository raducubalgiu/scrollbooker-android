package com.example.scrollbooker.ui.profile.tabs.about

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun BusinessMediaGallery(
    mediaFiles: List<BusinessMediaFile>?,
    modifier: Modifier = Modifier
) {
    if (mediaFiles.isNullOrEmpty()) {
        Text(
            text = stringResource(R.string.notFoundBusinessMedia),
            style = bodyMedium,
            color = OnBackground.copy(alpha = 0.45f)
        )
        return
    }

    Column(modifier = modifier.fillMaxWidth()) {
        mediaFiles.forEach { media ->
            AsyncImage(
                model = media.thumbnailUrl,
                contentDescription = "Business Media Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .aspectRatio(16f / 9f)
                    .clip(ShapeDefaults.Medium)
            )
        }
    }
}