package com.example.scrollbooker.ui.profile.tabs.info

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile

@Composable
fun BusinessMediaGallery(
    mediaFiles: List<BusinessMediaFile>?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        mediaFiles?.forEach { media ->
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