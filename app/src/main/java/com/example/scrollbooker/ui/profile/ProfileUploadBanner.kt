package com.example.scrollbooker.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.core.presentation.GlobalUploadStatus

@Composable
fun ProfileUploadBanner(
    uploadStatus: GlobalUploadStatus,
    onDismissError: () -> Unit
) {
    AnimatedVisibility(visible = uploadStatus !is GlobalUploadStatus.Idle) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (uploadStatus) {
                    is GlobalUploadStatus.Uploading -> {
                        AsyncImage(
                            model = uploadStatus.coverUri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sharing post... ${(uploadStatus.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { uploadStatus.progress },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    is GlobalUploadStatus.Success -> {
                        Text(
                            text = "🎉 Post published successfully!",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    is GlobalUploadStatus.Error -> {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "❌ Upload failed",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = uploadStatus.message,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                        TextButton(onClick = onDismissError) {
                            Text("Retry")
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
