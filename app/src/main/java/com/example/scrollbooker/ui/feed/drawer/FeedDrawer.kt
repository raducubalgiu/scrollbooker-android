package com.example.scrollbooker.ui.feed.drawer

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FeedDrawer(
    isDrawerOpen: Boolean,
    onClose: () -> Unit
) {
//    var selected by rememberSaveable(selectedFromVm) {
//        mutableStateOf(selectedFromVm)
//    }
//
//    LaunchedEffect(isDrawerOpen) {
//        if(!isDrawerOpen) {
//            viewModel.setSelectedBusinessTypes(selected)
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(BasePadding),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(Modifier.weight(1f)) {
//            when(val businessDomains = businessDomainsState) {
//                is FeatureState.Success -> {
//                    LazyColumn {
//                        item { FeedDrawerHeader() }
//
//                        itemsIndexed(businessDomains.data) { index, businessDomain ->
//                            var isExpanded by remember { mutableStateOf(false) }
//
//                            Accordion(
//                                modifier = Modifier.padding(bottom = BasePadding),
//                                title = businessDomain.shortName,
//                                isExpanded = isExpanded,
//                                onSetExpanded = { isExpanded = !isExpanded },
//                                containerColor = Color(0xFF1C1C1C),
//                                contentColor = Color(0xFFAAAAAA)
//                            ) {
//                                businessDomain.businessTypes.forEachIndexed { index, bt ->
//                                    InputCheckbox(
//                                        containerColor = Color.Transparent,
//                                        contentColor = Color(0xFFE0E0E0),
//                                        checked = selected.contains(bt.id),
//                                        onCheckedChange = {
//                                            selected = if(bt.id in selected) {
//                                                selected - bt.id
//                                            } else {
//                                                selected + bt.id
//                                            }
//                                        },
//                                        headLine = bt.plural,
//                                        height = 60.dp,
//                                        maxLines = 2,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//                else -> Unit
//            }
//        }
//
//        FeedDrawerActions(
//            isClearEnabled = selectedFromVm.isNotEmpty() || selected.isNotEmpty(),
//            isConfirmEnabled = selectedFromVm != selected,
//            onClear = { selected = emptySet() },
//            onConfirm = {
//                viewModel.pauseAllNow()
//                onClose()
//            },
//        )
//    }
}