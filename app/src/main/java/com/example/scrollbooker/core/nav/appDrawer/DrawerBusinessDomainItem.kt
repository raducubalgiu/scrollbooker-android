package com.example.scrollbooker.core.nav.appDrawer

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.core.nav.MainUIViewModel
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.businessDomain.domain.model.BusinessDomain
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.util.FeatureState

@Composable
fun DrawerBusinessDomainItem(
    selectedBusinessTypes: Set<Int>,
    viewModel: MainUIViewModel,
    businessDomain: BusinessDomain,
    onSetBusinessType: (Int) -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val businessTypesDomainState by viewModel.businessTypesByBusinessDomainState.collectAsState()
    val businessTypesState = businessTypesDomainState[businessDomain.id]

    val inputHeight = 60.dp

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
                onClick = {
                    if(!viewModel.hasBusinessTypesForMain(businessDomain.id)) {
                        viewModel.fetchBusinessTypesByBusinessDomain(businessDomain.id)
                    }
                    isExpanded = !isExpanded
                }
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = BasePadding,
                    horizontal = SpacingXL
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
        when(businessTypesState) {
            is FeatureState.Error -> Unit
            is FeatureState.Loading -> DrawerBusinessTypeShimmer()
            is FeatureState.Success<*> -> {
                val businessTypes = (businessTypesState as FeatureState.Success).data

                if(businessTypes.isEmpty()) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(inputHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.notFoundAnyResult),
                            color = Color(0xFFAAAAAA)
                        )
                    }
                } else {
                    LazyColumn(modifier = Modifier.height(inputHeight * businessTypes.size)) {
                        items(businessTypes) { businessType ->
                            InputCheckbox(
                                modifier = Modifier.background(Color(0xFF121212)),
                                contentColor = Color(0xFFE0E0E0),
                                checked = selectedBusinessTypes.contains(businessType.id),
                                onCheckedChange = { onSetBusinessType(businessType.id) },
                                headLine = businessType.plural,
                                height = inputHeight
                            )
                        }
                    }
                }
            }
            null -> Unit
        }
    }

    if(!isExpanded) {
        Spacer(Modifier.height(BasePadding))
    }
}