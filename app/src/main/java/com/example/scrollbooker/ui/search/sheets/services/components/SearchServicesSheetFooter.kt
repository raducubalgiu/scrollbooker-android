package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun SearchServicesSheetFooter(
    onFilter: () -> Unit,
    onClear: () -> Unit
) {
    val days = listOf("Astăzi", "Mâine", "Dupa 18:00", "În Weekend")

    Column {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = BasePadding),
            color = Divider,
            thickness = 0.55.dp
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = BasePadding)
        ) {
            item {
                Surface(
                    shadowElevation = 0.55.dp,
                    shape = ShapeDefaults.Medium
                ) {
                    MainButtonOutlined(
                        title = "Data si ora",
                        onClick = {},
                        icon = painterResource(R.drawable.ic_clock_outline),
                        iconColor = Color.Gray,
                        trailingIcon = Icons.Default.KeyboardArrowDown,
                        showTrailingIcon = true,
                        contentPadding = PaddingValues(vertical = SpacingM, horizontal = SpacingXXL),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceBG,
                            contentColor = OnBackground
                        ),
                        border = BorderStroke(1.dp, Color.Transparent),
                        shape = ShapeDefaults.Medium,
                    )
                }

                Spacer(Modifier.width(BasePadding))
            }

            itemsIndexed(days) { index, day ->
                MainButtonOutlined(
                    title = day,
                    onClick = {},
                    shape = ShapeDefaults.Medium,
                    contentPadding = PaddingValues(vertical = SpacingM, horizontal = SpacingXXL)
                )

                if(index <= days.size) {
                    Spacer(Modifier.width(BasePadding))
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(BasePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                TextButton(
                    onClick = onClear,
                    enabled = false
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        style = titleMedium,
                        color = OnBackground
                    )
                }
            }

            Button(
                contentPadding = PaddingValues(
                    vertical = BasePadding,
                    horizontal = SpacingXXL
                ),
                onClick = onFilter
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(SpacingXS))

                    Text(
                        text = stringResource(R.string.search),
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}