package com.example.scrollbooker.components.core.sheet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    enableCloseButton: Boolean = false,
    showHeader: Boolean = true,
    headerTitle: String = "",
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if( it == SheetValue.Hidden) {
                onDismiss()
            }
            true
        }
    )

    val isDarkTheme = isSystemInDarkTheme()
    val containerColor = if(isDarkTheme) SurfaceBG else Background
    val contentColor = if(isDarkTheme) OnSurfaceBG else OnBackground
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            sheetState.show()
        }
    }

    if(showBottomSheet) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = { onDismiss },
            containerColor = containerColor,
            sheetState = sheetState,
            modifier = Modifier.fillMaxWidth().then(modifier)
        ) {
            if(showHeader) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box{
                        Box(modifier = Modifier.padding(BasePadding)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.Transparent
                            )
                        }
                    }
                    Text(
                        text = headerTitle,
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(modifier = Modifier
                        .clickable(
                            onClick = {
                                if(enableCloseButton) {
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        onDismiss()
                                    }
                                }
                            }
                        )
                    ) {
                        Box(modifier = Modifier.padding(BasePadding)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = if(enableCloseButton) contentColor else Color.Transparent
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(top = if(showHeader) 0.dp else BasePadding)) {
                content()
            }

            Spacer(Modifier.height(SpacingXXL))
        }
    }
}