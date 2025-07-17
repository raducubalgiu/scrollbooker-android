package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputCheckbox
import com.example.scrollbooker.core.nav.MainUIViewModel
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.ui.theme.headlineMedium
import androidx.compose.runtime.getValue

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AppDrawer(
    viewModel: MainUIViewModel,
    businessDomainsState: FeatureState<List<BusinessDomain>>
) {
    val selectedBusinessTypes by viewModel.selectedBusinessTypes.collectAsState()

    val businessTypes = listOf(
        BusinessType(
            id = 1,
            name = "Service-uri auto",
            businessDomainId = 3,
            plural = "Service-uri auto"
        ),
        BusinessType(
            id = 2,
            name = "Statii ITP",
            businessDomainId = 3,
            plural = "Statii ITP"
        ),
        BusinessType(
            id = 3,
            name = "Frizerie",
            businessDomainId = 1,
            plural = "Frizerii"
        ),
        BusinessType(
            id = 4,
            name = "Salon de infrumusetare",
            businessDomainId = 1,
            plural = "Saloane de infrumusetare"
        )
    )

    BoxWithConstraints {
        val drawerWidth = maxWidth * 0.8f

        ModalDrawerSheet(
            modifier = Modifier.width(drawerWidth),
            drawerContainerColor = Color(0xFF121212)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(SpacingXL),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    when(val businessDomains = businessDomainsState) {
                        is FeatureState.Success -> {
                            LazyColumn {
                                item {
                                    Text(
                                        modifier = Modifier.padding(vertical = SpacingXL),
                                        style = headlineMedium,
                                        color = Color(0xFFE0E0E0),
                                        fontWeight = FontWeight.SemiBold,
                                        text = "Alege ce vrei să vezi în Feed"
                                    )

                                    Spacer(Modifier.height(BasePadding))
                                }

                                itemsIndexed(businessDomains.data) { index, businessDomain ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(shape = ShapeDefaults.ExtraLarge)
                                            .background(Color(0xFF1C1C1C)),
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
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = null,
                                                tint = Color(0xFF3A3A3A)
                                            )
                                        }
                                    }

//                                    Spacer(Modifier.height(BasePadding))

                                    businessTypes.mapIndexed { index, businessType ->
                                        val elements = businessTypes.filter { businessType.businessDomainId == businessDomain.id }

                                        if(elements.isNotEmpty()) {
                                            InputCheckbox(
                                                modifier = Modifier.background(Color(0xFF121212)),
                                                checked = selectedBusinessTypes.contains(businessType.id),
                                                onCheckedChange = { viewModel.setBusinessType(businessType.id) },
                                                headLine = businessType.plural,
                                                contentColor = Color(0xFFE0E0E0)
                                            )

                                            if(index < elements.size - 1) {
                                                HorizontalDivider(
                                                    color = Color(0xFF3A3A3A),
                                                    thickness = 0.55.dp
                                                )
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(BasePadding))
                                }
                            }
                        }
                        else -> Unit
                    }
                }

                MainButton(
                    onClick = {},
                    title = stringResource(R.string.filter),
                    //shape = RoundedCornerShape(2.dp)
                )
            }
        }
    }
}