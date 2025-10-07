package com.example.scrollbooker.components.core.inputs
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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge

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
    isRequired: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var parentWidth by remember { mutableIntStateOf(0) }

    val rotation by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f,
        label = "ArrowRotation"
    )

    val selected = options.find { it.value == selectedOption }
    val hasValue = selected != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
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
                        vertical = 2.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    if(hasValue && label != null) {
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

                    if(selectedOption.isBlank() || selectedOption == "null") {
                        Text(
                            text = placeholder,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = if(expanded) Primary else Color.Gray,
                            fontSize = 15.sp
                        )
                    } else {
                        Text(
                            text = selected?.name ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = OnSurfaceBG,
                        )
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
                            imageVector = Icons.Default.KeyboardArrowUp,
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
                    .background(SurfaceBG)
                    .width(with(LocalDensity.current) { parentWidth.toDp() })
            ) {
                options.forEach { option ->
                    val isSelected = selected?.value == option.value

                    DropdownMenuItem(
                        enabled = isEnabled,
                        modifier = Modifier.background(
                            if(isSelected) Primary.copy(alpha = 0.7f) else Color.Transparent),
                        text = {
                            Text(
                                text = option.name ?: placeholder,
                                color = if(isSelected) OnPrimary else OnSurfaceBG
                            )
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

    if(isRequired && selectedOption.isEmpty()) {
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
                    text = stringResource(R.string.requiredValidationMessage),
                    color = Error,
                    style = bodyMedium
                )
            }
        }
    }
}

data class Option(
    val value: String?,
    val name: String?
)