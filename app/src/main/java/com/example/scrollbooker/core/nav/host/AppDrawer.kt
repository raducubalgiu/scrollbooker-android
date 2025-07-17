package com.example.scrollbooker.core.nav.host

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AppDrawer(
    businessTypesState: FeatureState<List<BusinessType>>
) {
    var search by remember { mutableStateOf("") }

    BoxWithConstraints {
        val drawerWidth = maxWidth * 0.7f

        ModalDrawerSheet(
            modifier = Modifier
                .width(drawerWidth),
            drawerContainerColor = Color(0xFF121212)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(BasePadding)
            ) {
                SearchBar(
                    value = search,
                    containerColor = Color(0xFF1C1C1C),
                    contentColor = Color(0xFF3A3A3A),
                    onValueChange = { search = it },
                    placeholder = "Ce cauti?",
                    onSearch = {}
                )

                HorizontalDivider(modifier = Modifier
                    .padding(vertical = BasePadding),
                    color = Color(0xFF3A3A3A)
                )

                Text(
                    style = titleMedium,
                    color = Color(0xFF3A3A3A),
                    fontWeight = FontWeight.Normal,
                    text = "Filtrează conținutul video"
                )

                Spacer(Modifier.height(BasePadding))

                when(val businessTypes = businessTypesState) {
                    is FeatureState.Success -> {
                        LazyColumn {
                            itemsIndexed(businessTypes.data) { index, businessType ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            vertical = SpacingS
                                        )
                                        .clip(shape = ShapeDefaults.ExtraLarge)
                                        .background(Color(0xFF1C1C1C)),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                BasePadding
                                            )
                                    ) {
                                        Text(
                                            text = businessType.plural,
                                            color = Color(0xFFAAAAAA)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(SpacingS))
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}