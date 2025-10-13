package com.example.scrollbooker.ui.feed.drawer

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import androidx.compose.runtime.getValue
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.theme.BackgroundDark

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FeedDrawer(
    viewModel: FeedScreenViewModel,
    businessDomainsState: FeatureState<List<BusinessDomainsWithBusinessTypes>>,
    onFilter: () -> Unit
) {
    val selectedBusinessTypes by viewModel.selectedBusinessTypes.collectAsState()

    BoxWithConstraints {
        val screenWidth = maxWidth

        ModalDrawerSheet(
            modifier = Modifier.width(screenWidth * 0.85f),
            drawerContainerColor = BackgroundDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = SpacingXL,
                        vertical = BasePadding
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    when(val businessDomains = businessDomainsState) {
                        is FeatureState.Success -> {
                            LazyColumn {
                                item {
                                    FeedDrawerHeader()
                                }

                                itemsIndexed(businessDomains.data) { index, businessDomain ->
                                    BusinessDomainItem(
                                        selectedBusinessTypes = selectedBusinessTypes,
                                        businessDomain = businessDomain,
                                        onSetBusinessType = {
                                            viewModel.setBusinessType(it)
                                        }
                                    )
                                }
                            }
                        }
                        else -> Unit
                    }
                }

                FeedDrawerActions(
                    isResetVisible = selectedBusinessTypes.isNotEmpty(),
                    onReset = { viewModel.clearBusinessTypes() },
                    onFilter = onFilter
                )
            }
        }
    }
}