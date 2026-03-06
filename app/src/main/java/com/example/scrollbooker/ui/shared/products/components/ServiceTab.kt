package com.example.scrollbooker.ui.shared.products.components
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge

@Composable
fun ServiceTab(
    isSelected: Boolean,
    serviceName: String,
    onClick: () -> Unit,
    shape: Shape = ShapeDefaults.ExtraLarge,
    paddingValues: PaddingValues = ButtonDefaults.ContentPadding,
    style: TextStyle = bodyLarge,
    fontSize: TextUnit = 16.sp
) {
    Button(
        modifier = Modifier.padding(vertical = 8.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isSelected) SurfaceBG else Color.Transparent,
            contentColor = if (isSelected) OnSurfaceBG else Color.Gray
        ),
        contentPadding = paddingValues,
        shape = shape,
    ) {
        Text(
            text = serviceName,
            style = style,
            fontSize = fontSize,
            fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold,
        )
    }
}