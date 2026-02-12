package com.example.scrollbooker.ui.feed.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.accordion.Accordion
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.ui.feed.FeedScreenViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FeedDrawer(
    viewModel: FeedScreenViewModel,
    isDrawerOpen: Boolean,
    businessDomainsState: FeatureState<List<BusinessDomain>>,
    selectedFromVm: Set<Int>,
    onClose: () -> Unit
) {
    var selected by rememberSaveable(selectedFromVm) {
        mutableStateOf(selectedFromVm)
    }

    LaunchedEffect(isDrawerOpen) {
        if(!isDrawerOpen) {
            viewModel.setSelectedBusinessTypes(selected)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(BasePadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            when(val businessDomains = businessDomainsState) {
                is FeatureState.Success -> {
                    LazyColumn {
                        item { FeedDrawerHeader() }

                        itemsIndexed(businessDomains.data) { index, businessDomain ->
                            var isExpanded by remember { mutableStateOf(false) }

                            Accordion(
                                modifier = Modifier.padding(bottom = BasePadding),
                                title = businessDomain.shortName,
                                isExpanded = isExpanded,
                                onSetExpanded = { isExpanded = !isExpanded },
                                containerColor = Color(0xFF1C1C1C),
                                contentColor = Color(0xFFAAAAAA)
                            ) {
                                businessDomain.businessTypes.forEachIndexed { index, bt ->
                                    InputCheckbox(
                                        containerColor = Color.Transparent,
                                        contentColor = Color(0xFFE0E0E0),
                                        checked = selected.contains(bt.id),
                                        onCheckedChange = {
                                            selected = if(bt.id in selected) {
                                                selected - bt.id
                                            } else {
                                                selected + bt.id
                                            }
                                        },
                                        headLine = bt.plural,
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
            isClearEnabled = selectedFromVm.isNotEmpty() || selected.isNotEmpty(),
            isConfirmEnabled = selectedFromVm != selected,
            onClear = { selected = emptySet() },
            onConfirm = {
                viewModel.pauseAllNow()
                onClose()
            },
        )
    }
}