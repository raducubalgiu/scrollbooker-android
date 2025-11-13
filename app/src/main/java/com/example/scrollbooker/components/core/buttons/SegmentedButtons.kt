package com.example.scrollbooker.components.core.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.labelLarge

@Composable
fun SegmentedButtons(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(50))
        .background(SurfaceBG)
        .padding(vertical = 4.dp, horizontal = 8.dp)
        .then(modifier)
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth(),
            space = 0.dp
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = index == selectedIndex

                SegmentedButton(
                    selected = isSelected,
                    onClick = { onClick(index) },
                    icon = {},
                    shape = RoundedCornerShape(50),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = Primary,
                        activeContentColor = OnPrimary,
                        activeBorderColor = Primary,
                        inactiveContainerColor = SurfaceBG,
                        inactiveContentColor = OnSurfaceBG,
                        inactiveBorderColor = SurfaceBG,
                    )
                ) {
                    Text(
                        text = title,
                        style = labelLarge,
                        fontSize = 16.sp,
                        fontWeight = if(isSelected) FontWeight.Bold else FontWeight.SemiBold
                    )
                }
            }
        }
    }
}