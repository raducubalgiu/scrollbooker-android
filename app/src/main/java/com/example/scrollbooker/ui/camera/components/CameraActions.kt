package com.example.scrollbooker.ui.camera.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R

@Composable
fun CameraActions(
    isRecording: Boolean,
    onSwitchCamera: () -> Unit,
    onRecord: () -> Unit,
    onLongPressRecord: (() -> Unit)? = null
) {
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
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Flip camera"
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