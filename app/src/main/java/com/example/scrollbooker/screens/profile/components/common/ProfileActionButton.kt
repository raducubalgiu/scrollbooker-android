package com.example.scrollbooker.screens.profile.components.common
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ProfileActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    containerColor: Color = SurfaceBG,
    contentColor: Color = OnSurfaceBG,
    content: @Composable () -> Unit,
) {
    Button(
        modifier = modifier.height(50.dp),
        onClick = onClick,
        enabled = isEnabled,
        shape = RoundedCornerShape(size = 7.5.dp),
        contentPadding = PaddingValues(
            vertical = 14.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        content()
    }
}