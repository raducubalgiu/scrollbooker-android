package com.example.scrollbooker.ui.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeader(
    modifier: Modifier = Modifier,
    headline: String,
    subHeadline: String,
    sheetValue: SheetValue,
    onMapToggle: () -> Unit,
    onClick: () -> Unit
) {
    val isSheetExpanded = sheetValue == SheetValue.Expanded
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = BasePadding)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                clip = false
            )
            .clip(shape = CircleShape)
            .background(Background)
            .clickable { onClick() }
            .then(modifier)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.padding(SpacingS),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(SpacingM)
                        .size(35.dp),
                    painter = painterResource(R.drawable.ic_search_solid),
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = SpacingS),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = headline,
                    fontWeight = FontWeight.SemiBold,
                    style = titleMedium,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(SpacingXS))
                Text(
                    text = subHeadline,
                    color = Color.Gray,
                    style = bodyLarge
                )
            }
            Box(
                modifier = Modifier
                    .padding(SpacingS)
                    .border(
                        width = 1.dp,
                        color = Divider,
                        shape = CircleShape
                    )
                    .clickable(
                        onClick = { onMapToggle() },
                        indication = null,
                        interactionSource = interactionSource
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .padding(SpacingM)
                        .size(22.5.dp),
                    painter = painterResource(
                        if(isSheetExpanded) R.drawable.ic_map_outline
                        else R.drawable.ic_list_bullet_outline
                    ),
                    contentDescription = null
                )
            }
        }
    }
}