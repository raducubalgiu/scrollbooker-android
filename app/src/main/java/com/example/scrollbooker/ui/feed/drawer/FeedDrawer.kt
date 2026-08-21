package com.example.scrollbooker.ui.feed.drawer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedDrawer(
    serviceDomains: FeatureState<List<ServiceDomain>>,
    selectedServiceIds: Set<Int>,
    isDrawerOpen: Boolean,
    onConfirm: (Set<Int>) -> Unit
) {
    var selected by rememberSaveable(selectedServiceIds) {
        mutableStateOf(selectedServiceIds)
    }

    LaunchedEffect(isDrawerOpen) {
        if(!isDrawerOpen) {
            onConfirm(selected)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            when (val state = serviceDomains) {
                is FeatureState.Success -> {
                    LazyColumn {
                        item { FeedDrawerHeader() }

                        itemsIndexed(state.data) { index, serviceDomain ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = BasePadding)
                            ) {
                                Text(
                                    text = serviceDomain.name,
                                    style = bodyLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(BasePadding))

                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    serviceDomain.services.forEach { s ->
                                        val isSelected = selected.contains(s.id)

                                        val backgroundColor = if (isSelected) Primary else Color(0xFF1C1C1C)
                                        val textColor = if (isSelected) OnPrimary else Color(0xFFAAAAAA)

                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .background(backgroundColor)
                                                .clickable {
                                                    selected = if (s.id in selected) {
                                                        selected - s.id
                                                    } else {
                                                        selected + s.id
                                                    }
                                                }
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = s.name,
                                                style = bodyMedium,
                                                color = textColor
                                            )
                                        }
                                    }
                                }

                                if (index < state.data.lastIndex) {
                                    Spacer(modifier = Modifier.height(SpacingM))
                                }
                            }
                        }
                    }
                }
                else -> Unit
            }
        }

        FeedDrawerActions(
            isClearEnabled = selectedServiceIds.isNotEmpty() || selected.isNotEmpty(),
            isConfirmEnabled = selectedServiceIds != selected,
            onClear = { selected = emptySet() },
            onConfirm = { onConfirm(selected) },
        )
    }
}