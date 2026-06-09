package com.example.scrollbooker.ui.search.businessProfile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun NearbyBusinessImage(
    url: String?,
    username: String,
    modifier: Modifier = Modifier
) {
    val isPlaceholder = url.isNullOrEmpty() || url == "placeholder.jpg"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .clip(ShapeDefaults.Medium)
            .background(SurfaceBG)
    ) {
        if (!isPlaceholder) {
            // Componenta din Coil pentru încărcare asincronă de pe web
            AsyncImage(
                model = url,
                contentDescription = "Imagine business $username",
                contentScale = ContentScale.Crop, // Taie și umple containerul fără să deformeze imaginea
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Placeholder vizual în caz că afacerea nu are imagine încărcată (Afișează prima literă din username)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground.copy(alpha = 0.4f)
                )
            }
        }
    }
}
