package com.example.scrollbooker.components.core.buttons
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    fullWidth: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    enabled: Boolean = true,
    title: String,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = ShapeDefaults.ExtraLarge
) {
    Button(
        onClick = onClick,
        modifier = if(fullWidth) modifier.fillMaxWidth() else modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = PaddingValues(BasePadding),
        shape = shape
    ) {
        if(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(17.dp),
                strokeWidth = 4.dp,
                color = Divider
            )
        } else {
            Text(
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold,
                text = title
            )
        }
    }
}