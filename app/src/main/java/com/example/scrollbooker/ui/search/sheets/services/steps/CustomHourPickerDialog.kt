package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleLarge
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter


@Composable
fun CustomHourPickerDialog(
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    isTimeValid: (LocalTime) -> Boolean = { true }
) {
    val hoursList = remember { (0..23).toList() }
    val hourFormatter = remember { DateTimeFormatter.ofPattern("HH:00") }

    val itemHeight = 45.dp
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialTime.hour)

    val selectedIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) {
                listState.firstVisibleItemIndex
            } else {
                val containerCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2
                visibleItemsInfo.minByOrNull {
                    val itemCenter = (it.offset + it.offset + it.size) / 2
                    kotlin.math.abs(itemCenter - containerCenter)
                }?.index ?: listState.firstVisibleItemIndex
            }
        }
    }

    val selectedHour = hoursList[selectedIndex]
    val isValid = remember(selectedHour) { isTimeValid(LocalTime.of(selectedHour, 0)) }

    AlertDialog(
        containerColor = Background,
        onDismissRequest = { onDismiss() },
        confirmButton = {},
        dismissButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight * 3),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight),
                        color = SurfaceBG.copy(alpha = 0.4f),
                        shape = ShapeDefaults.Small
                    ) {}

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = itemHeight),
                        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(hoursList.size) { index ->
                            val hour = hoursList[index]
                            val isCurrentSelection = index == selectedIndex

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(itemHeight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = LocalTime.of(hour, 0).format(hourFormatter),
                                    style = if (isCurrentSelection) titleLarge else bodyLarge,
                                    color = OnBackground,
                                    fontWeight = if (isCurrentSelection) FontWeight.ExtraBold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .alpha(if (isCurrentSelection) 1f else 0.4f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = ShapeDefaults.Medium,
                        border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnBackground)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = { onTimeSelected(LocalTime.of(selectedHour, 0)) },
                        enabled = isValid,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = ShapeDefaults.Medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OnBackground,
                            contentColor = Background,
                            disabledContainerColor = OnBackground.copy(alpha = 0.15f),
                            disabledContentColor = OnBackground.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.add),
                            style = bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}