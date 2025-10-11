package com.example.scrollbooker.ui.camera.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun CameraActions(
    isRecording: Boolean,
    onSwitchCamera: () -> Unit,
    onRecord: () -> Unit,
    onLongPressRecord: (() -> Unit)? = null,
    onOpenMediaLibrary: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .clickable(
                    onClick = onOpenMediaLibrary,
                    interactionSource = interactionSource,
                    indication = null
                )
            ) {
                AsyncImage(
                    model = null,
                    contentDescription = "Media Library",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = ShapeDefaults.Medium)
                        .background(BackgroundDark)
                        .border(2.dp, Color.Gray, ShapeDefaults.Medium),
                )
            }

            RecordButton(
                isRecording = isRecording,
                onTap = onRecord,
                onLongPress = onLongPressRecord
            )

            IconButton(onClick = onSwitchCamera) {
                Icon(
                    painter = painterResource(R.drawable.ic_reload),
                    contentDescription = "Flip camera",
                    modifier = Modifier.size(30.dp),
                    tint = Color(0xFFE0E0E0),
                )
            }
        }
    }
}