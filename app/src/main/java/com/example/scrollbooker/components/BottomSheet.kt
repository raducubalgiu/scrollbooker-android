package com.example.scrollbooker.components
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
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
                    Box{ Box(modifier = Modifier.padding(BasePadding)) }
                    Text(
                        text = headerTitle,
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(modifier = Modifier
                        .clickable(
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismiss()
                                }
                            }
                        )
                    ) {
                        Box(modifier = Modifier.padding(BasePadding)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = null
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