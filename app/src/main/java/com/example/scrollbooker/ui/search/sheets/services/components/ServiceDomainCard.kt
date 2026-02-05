package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServiceDomainCard(
    name: String,
    thumbnailUrl: String?,
    onClick: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(shape = ShapeDefaults.Medium)
                .clickable {
                    onClick()
                }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBG)
            )

            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(Modifier.height(SpacingS))

        Text(
            text = name,
            style = titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}