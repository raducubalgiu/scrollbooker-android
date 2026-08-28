package com.example.scrollbooker.ui.feed.drawer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun FeedDrawer(
    serviceDomains: FeatureState<List<ServiceDomain>>,
    selectedServiceIds: Set<Int>,
    onlyVideoReviews: Boolean,
    isDrawerOpen: Boolean,
    onConfirm: (Set<Int>, Boolean) -> Unit
) {
    var selected by rememberSaveable(selectedServiceIds) {
        mutableStateOf(selectedServiceIds)
    }
    var onlyVideos by rememberSaveable(onlyVideoReviews) {
        mutableStateOf(onlyVideoReviews)
    }

    LaunchedEffect(isDrawerOpen) {
        if (!isDrawerOpen) {
            onConfirm(selected, onlyVideos)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = BasePadding),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            when (val state = serviceDomains) {
                is FeatureState.Loading -> FeedDrawerSkeleton()
                is FeatureState.Error -> {
                    Column {
                        FeedDrawerHeader()

                        Text(
                            text = stringResource(R.string.somethingWentWrong),
                            style = bodyMedium,
                            color = Color(0xFFAAAAAA)
                        )
                    }
                }
                is FeatureState.Success -> {
                    LazyColumn {
                        item { FeedDrawerHeader() }

                        item {
                            FeedDrawerVideoReviewsToggle(
                                checked = onlyVideos,
                                onCheckedChange = { onlyVideos = it }
                            )

                            Spacer(Modifier.height(SpacingXL))

                            HorizontalDivider(
                                color = Color(0xFF3A3A3A),
                                thickness = 0.55.dp
                            )

                            Spacer(Modifier.height(SpacingXL))

                            Text(
                                text = stringResource(R.string.categories),
                                style = bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF777777)
                            )

                            Spacer(Modifier.height(BasePadding))
                        }

                        itemsIndexed(state.data) { index, serviceDomain ->
                            FeedDrawerDomainSection(
                                domain = serviceDomain,
                                selectedServiceIds = selected,
                                onToggleService = { serviceId ->
                                    selected = if (serviceId in selected) {
                                        selected - serviceId
                                    } else {
                                        selected + serviceId
                                    }
                                }
                            )

                            if (index < state.data.lastIndex) {
                                Spacer(Modifier.height(SpacingXL))
                                HorizontalDivider(
                                    color = Color(0xFF2A2A2A),
                                    thickness = 0.55.dp
                                )
                                Spacer(Modifier.height(SpacingXL))
                            } else {
                                Spacer(Modifier.height(BasePadding))
                            }
                        }
                    }
                }
            }
        }

        FeedDrawerActions(
            isClearEnabled = selected.isNotEmpty() || onlyVideos,
            isConfirmEnabled = selected != selectedServiceIds || onlyVideos != onlyVideoReviews,
            selectedCount = selected.size,
            onClear = {
                selected = emptySet()
                onlyVideos = false
            },
            onConfirm = { onConfirm(selected, onlyVideos) },
        )
    }
}
