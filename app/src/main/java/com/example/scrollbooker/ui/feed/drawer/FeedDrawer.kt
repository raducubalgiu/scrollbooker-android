package com.example.scrollbooker.ui.feed.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

@SuppressLint("UnusedBoxWithConstraintsScope")
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

//    LaunchedEffect(isDrawerOpen) {
//        if(!isDrawerOpen) {
//            //viewModel.setSelectedBusinessTypes(selected)
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            when(val state = serviceDomains) {
                is FeatureState.Success -> {
                    LazyColumn {
                        item { FeedDrawerHeader() }

                        itemsIndexed(state.data) { index, serviceDomain ->
                            var isExpanded by remember { mutableStateOf(false) }

                            Accordion(
                                modifier = Modifier.padding(bottom = BasePadding),
                                title = serviceDomain.name,
                                isExpanded = isExpanded,
                                onSetExpanded = { isExpanded = !isExpanded },
                                containerColor = Color(0xFF1C1C1C),
                                contentColor = Color(0xFFAAAAAA)
                            ) {
                                serviceDomain.services.forEachIndexed { index, s ->
                                    InputCheckbox(
                                        containerColor = Color.Transparent,
                                        contentColor = Color(0xFFE0E0E0),
                                        checked = selected.contains(s.id),
                                        onCheckedChange = {
                                            selected = if(s.id in selected) {
                                                selected - s.id
                                            } else {
                                                selected + s.id
                                            }
                                        },
                                        headLine = s.name,
                                        height = 60.dp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
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