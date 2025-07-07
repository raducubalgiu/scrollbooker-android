package com.example.scrollbooker.components.core.sheet
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {

    val isDarkTheme = isSystemInDarkTheme()
    val containerColor = if(isDarkTheme) SurfaceBG else Background
    val contentColor = if(isDarkTheme) OnSurfaceBG else OnBackground

    ModalBottomSheet(
        modifier = modifier.fillMaxWidth().then(modifier),
        dragHandle = null,
        onDismissRequest = onClose,
        containerColor = containerColor,
        contentColor = contentColor,
        sheetState = sheetState,
    ) {
        content()
    }
}