package com.example.scrollbooker.ui.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.ui.theme.headlineMedium
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.feed.FeedViewModel
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MainDrawer(
    viewModel: MainUIViewModel,
    feedViewModel: FeedViewModel,
    businessDomainsState: FeatureState<List<BusinessDomain>>,
    onClose: () -> Unit
) {
    val selectedBusinessTypes by viewModel.selectedBusinessTypes.collectAsState()
    val updatedBusinessTypes by feedViewModel.selectedBusinessTypes.collectAsState()

    BoxWithConstraints {
        val screenWidth = maxWidth

        ModalDrawerSheet(
            modifier = Modifier
                .width(screenWidth * 0.85f),
            drawerContainerColor = Color(0xFF121212)
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
                                    Text(
                                        modifier = Modifier.padding(
                                            top = SpacingXL,
                                            end = SpacingS
                                        ),
                                        style = headlineMedium,
                                        color = Color(0xFFE0E0E0),
                                        fontWeight = FontWeight.SemiBold,
                                        text = stringResource(R.string.chooseWhatDoYouWantToSeeInFeed)
                                    )
                                    Spacer(Modifier.height(40.dp))
                                }

                                itemsIndexed(businessDomains.data) { index, businessDomain ->
                                    MainDrawerBusinessDomainItem(
                                        viewModel = viewModel,
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

                Column {
                    HorizontalDivider(
                        color = Color(0xFF3A3A3A),
                        thickness = 0.55.dp
                    )
                    Spacer(Modifier.height(BasePadding))

                    AnimatedVisibility(
                        visible = selectedBusinessTypes.isNotEmpty(),
                        enter = slideInVertically(initialOffsetY = { -20 }) + fadeIn(animationSpec = tween(250)),
                        exit = slideOutVertically(targetOffsetY = { -20 }) + fadeOut(animationSpec = tween(250))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = BasePadding),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(onClick = { viewModel.clearBusinessTypes() }) {
                                Text(
                                    text = stringResource(R.string.clearFilters),
                                    color = Error,
                                    style = bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    MainButton(
                        onClick = {
                            feedViewModel.updateBusinessTypes(selectedBusinessTypes)
                            onClose()
                        },
                        title = stringResource(R.string.filter),
                        enabled = selectedBusinessTypes != updatedBusinessTypes,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF6F00),
                            contentColor = Color(0xFFE0E0E0),
                            disabledContainerColor = Color(0xFF1C1C1C),
                            disabledContentColor = Color(0xFF3A3A3A),
                        )
                    )
                }
            }
        }
    }
}