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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.ui.feed.FeedScreenViewModel
import com.example.scrollbooker.ui.theme.BackgroundDark

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FeedDrawer(
    viewModel: FeedScreenViewModel,
    businessDomainsState: FeatureState<List<BusinessDomainsWithBusinessTypes>>,
    onClose: () -> Unit
) {
    val selectedFromVm by viewModel.selectedBusinessTypes.collectAsState()

    var selected by rememberSaveable(selectedFromVm) {
        mutableStateOf(selectedFromVm)
    }

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
                                        selectedBusinessTypes = selected,
                                        businessDomain = businessDomain,
                                        onSetBusinessType = { typeId ->
                                           selected = if(typeId in selected) {
                                               selected - typeId
                                           } else {
                                               selected + typeId
                                           }
                                        }
                                    )
                                }
                            }
                        }
                        else -> Unit
                    }
                }

                FeedDrawerActions(
                    isResetVisible = selectedFromVm.isNotEmpty() || selected.isNotEmpty(),
                    onReset = {
                        selected = emptySet()
                        viewModel.clearBusinessTypes()
                        onClose()
                    },
                    onFilter = {
                        viewModel.setSelectedBusinessTypes(selected)
                        onClose()
                    },
                    isEnabled = selectedFromVm != selected
                )
            }
        }
    }
}