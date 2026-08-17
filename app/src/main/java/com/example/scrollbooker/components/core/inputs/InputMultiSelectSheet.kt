package com.example.scrollbooker.components.core.inputs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMultiSelectSheet(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    options: List<Option>,
    selectedValues: Set<String>,
    isSingleSelect: Boolean,
    onConfirm: (Set<String>) -> Unit,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String = "",
    background: Color = SurfaceBG,
    color: Color = OnSurfaceBG
) {
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val selectedOptions = remember(selectedValues, options) {
        options.filter { it.value != null && it.value in selectedValues }
    }

    val hasValue = selectedOptions.isNotEmpty()

    val displayText = remember(selectedOptions) {
        selectedOptions.joinToString(" & ") { it.name.orEmpty() }
    }

    val placeholderColor = if (isError) MaterialTheme.colorScheme.error else Color.Gray

    fun closeSheet() {
        scope.launch { sheetState.hide() }
            .invokeOnCompletion { showSheet = false }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ShapeDefaults.Medium)
            .background(background)
            .clickable(enabled = isEnabled) { showSheet = true }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                if (hasValue && label != null) {
                    Text(
                        text = label,
                        style = labelLarge,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = SpacingXS, bottom = 2.dp)
                    )
                }

                Text(
                    text = if (hasValue) displayText else placeholder,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (hasValue) color else placeholderColor,
                    fontSize = if (hasValue) 16.sp else 14.sp,
                    fontWeight = if (hasValue) FontWeight.Normal else FontWeight.SemiBold
                )
            }

            if(isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(17.5.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
    }

    AnimatedVisibility(visible = isError) {
        Row(
            modifier = Modifier.padding(top = BasePadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Outlined.Warning, contentDescription = null, tint = Error)
            Spacer(Modifier.width(SpacingS))
            Text(text = errorMessage, color = Error, style = bodyMedium)
        }
    }

    if (showSheet) {
        var localSelectedValues by remember(showSheet) { mutableStateOf(selectedValues) }

        fun confirmAndClose() {
            onConfirm(localSelectedValues)
            closeSheet()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showSheet = false },
            containerColor = Background,
            contentColor = OnBackground,
            dragHandle = {}
        ) {
            Column(Modifier.fillMaxWidth()) {
                SheetHeader(
                    title = label.orEmpty(),
                    onClose = { closeSheet() }
                )

                Column(
                    modifier = Modifier
                        .padding(start = SpacingXL, end = SpacingM)
                        .navigationBarsPadding()
                        .padding(bottom = BasePadding)
                ) {
                    options.forEach { option ->
                        val optionValue = option.value ?: return@forEach
                        val isChecked = optionValue in localSelectedValues

                        fun toggleOption() {
                            localSelectedValues = if (isSingleSelect) {
                                setOf(optionValue)
                            } else {
                                if (isChecked) localSelectedValues - optionValue
                                else localSelectedValues + optionValue
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { toggleOption() }
                                .padding(vertical = SpacingM),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = option.name.orEmpty(), style = bodyLarge)

                            IconButton(onClick = { toggleOption() }) {
                                Icon(
                                    imageVector = if (isChecked) Icons.Filled.CheckCircle
                                    else Icons.Outlined.Circle,
                                    contentDescription = null,
                                    tint = if (isChecked) OnBackground else Divider
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(BasePadding))

                    MainButton(
                        onClick = { confirmAndClose() },
                        title = stringResource(R.string.add),
                        enabled = localSelectedValues.isNotEmpty()
                    )
                }
            }
        }
    }
}