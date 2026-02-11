package com.example.scrollbooker.ui.feed.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.ui.theme.BackgroundDark

@Composable
fun BusinessDomainItem(
    selectedBusinessTypes: Set<Int>,
    businessDomain: BusinessDomain,
    onSetBusinessType: (Int) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val inputHeight = 70.dp

    val rotation by animateFloatAsState(
        targetValue = if(isExpanded) 180f else 0f,
        label = "ArrowRotation"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = ShapeDefaults.ExtraLarge)
            .background(Color(0xFF1C1C1C))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { isExpanded = !isExpanded }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    BasePadding
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = businessDomain.shortName,
                color = Color(0xFFAAAAAA),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(BasePadding))
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.rotate(rotation),
                tint = Color(0xFF3A3A3A)
            )
        }
    }

    AnimatedVisibility(
        visible = isExpanded,
        enter = slideInVertically(initialOffsetY = { -20 }) + fadeIn(animationSpec = tween(250)),
        exit = slideOutVertically(targetOffsetY = { -20 }) + fadeOut(animationSpec = tween(250))
    ) {
        val businessTypes = businessDomain.businessTypes

        LazyColumn(
            modifier = Modifier
                .padding(vertical = SpacingS)
                .height(inputHeight * businessTypes.size),
            overscrollEffect = null
        ) {
            items(businessTypes) { businessType ->
                InputCheckbox(
                    modifier = Modifier.background(BackgroundDark),
                    contentColor = Color(0xFFE0E0E0),
                    checked = selectedBusinessTypes.contains(businessType.id),
                    onCheckedChange = { onSetBusinessType(businessType.id) },
                    headLine = businessType.plural,
                    height = inputHeight,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    if(!isExpanded) {
        Spacer(Modifier.height(BasePadding))
    }
}