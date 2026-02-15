package com.example.scrollbooker.components.core.inputs
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun InputSelect(
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    options: List<Option>,
    selectedOption: String,
    onValueChange: (String?) -> Unit,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String = "",
    displayLabel: Boolean = true,
    background: Color = SurfaceBG,
    color: Color = OnSurfaceBG
) {
    var expanded by remember { mutableStateOf(false) }
    var parentWidth by remember { mutableIntStateOf(0) }

    val rotation by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f,
        label = "ArrowRotation"
    )

    val selected: Option? = remember(selectedOption, options) {
        options.firstOrNull() { it.value == selectedOption }
    }

    val hasValue = selected != null

    val placeholderColor = when {
        isError -> MaterialTheme.colorScheme.error
        expanded -> LastMinute
        else -> Color.Gray
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Medium)
            .background(background)
            .clickable { expanded = true }
    ) {
        Box(modifier = modifier
            .onGloballyPositioned { coordinates ->
                parentWidth = coordinates.size.width
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = BasePadding,
                        vertical = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    if(hasValue && label != null && displayLabel) {
                        Text(
                            text = label,
                            style = labelLarge,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(
                                top = SpacingXS,
                                bottom = 2.dp
                            )
                        )
                    }

                    when {
                        selectedOption.isBlank() == true || selectedOption == "null" -> {
                            Text(
                                text = placeholder,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = placeholderColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        selectedOption.isNotBlank() -> {
                            Text(
                                text = selected?.name ?: "",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = color,
                            )
                        }
                        else -> {
                            Text(
                                text = placeholder,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = placeholderColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Column {
                    if(isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(17.5.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotation)
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .clip(shape = ShapeDefaults.Medium)
                    .background(background)
                    .width(with(LocalDensity.current) { parentWidth.toDp() })
            ) {
                options.forEach { option ->
                    val value = option.value

                    val isSelected = when {
                        value == null -> false
                        selectedOption != "null" -> selected?.value == value
                        else -> false
                    }

                    DropdownMenuItem(
                        enabled = isEnabled,
                        modifier = Modifier
                            .background(if(isSelected) LastMinute.copy(alpha = 0.2f) else Color.Transparent),
                        text = {
                            val hasDescription = option.description?.isNotEmpty() == true

                            Column(
                                modifier = Modifier.padding(vertical = SpacingS)
                            ) {
                                Text(
                                    text = option.name ?: placeholder,
                                    color = color,
                                    style = titleSmall,
                                    fontWeight = if(hasDescription) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(Modifier.padding(vertical = SpacingXS))

                                if(hasDescription) {
                                    Text(
                                        text = option.description,
                                        color = Color.Gray,
                                        style = bodySmall,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        },
                        onClick = {
                            onValueChange(option.value)
                            expanded = false
                        },
                    )
                }
            }
        }
    }

    AnimatedVisibility(visible = isError) {
        Column(modifier = Modifier
            .padding(top = BasePadding)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = Error
                )
                Spacer(Modifier.width(SpacingS))
                Text(
                    text = errorMessage,
                    color = Error,
                    style = bodyMedium
                )
            }
        }
    }
}

data class Option(
    val value: String?,
    val name: String?,
    val description: String? = null
)