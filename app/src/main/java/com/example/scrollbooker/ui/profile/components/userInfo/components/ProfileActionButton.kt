package com.example.scrollbooker.ui.profile.components.userInfo.components
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun ProfileActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    height: Dp = 50.dp,
    shape: Shape = ShapeDefaults.Small,
    containerColor: Color = SurfaceBG,
    contentColor: Color = OnSurfaceBG,
    content: @Composable () -> Unit,
) {
    Button(
        modifier = modifier.height(height),
        onClick = onClick,
        enabled = isEnabled,
        shape = shape,
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