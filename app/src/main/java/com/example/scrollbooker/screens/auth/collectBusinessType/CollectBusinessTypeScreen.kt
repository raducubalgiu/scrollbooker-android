package com.example.scrollbooker.screens.auth.collectBusinessType
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS

@Composable
fun CollectBusinessTypeScreen(
    viewModel: CollectBusinessTypeViewModel,
    onNext: (String) -> Unit
) {
    val pagingItems = viewModel.businessTypes.collectAsLazyPagingItems()
    var selectedBusinessTypeId by remember { mutableStateOf<Int?>(null) }
    var selectedBusinessName by remember { mutableStateOf("") }

    FormLayout(
        enableBack = false,
        isEnabled = selectedBusinessTypeId != null,
        headLine = stringResource(id = R.string.collectBusinessTypeHeadline),
        subHeadLine = stringResource(id = R.string.collectBusinessTypeSubHeadline),
        buttonTitle = stringResource(R.string.nextStep),
        onNext = { onNext(AuthRoute.CollectBusinessLocation.route) },
    ) {
        pagingItems.apply {
            when(loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> {
                    if(pagingItems.itemCount == 0) {
                        ErrorScreen()
                    }
                }
            }
        }

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Box(Modifier.padding(
                    start = SpacingXL,
                    end = SpacingXXL,
                    top = SpacingXXS,
                    bottom = SpacingXXS
                )) {
                    SearchBar(
                        value = "",
                        onValueChange = {},
                        placeholder = "Cauta categorie"
                    )
                }
            }

            items(pagingItems.itemCount) { index ->
                pagingItems[index]?.let { businessType ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(Background)
                            .selectable(
                                selected = selectedBusinessTypeId == businessType.id,
                                onClick = {
                                    selectedBusinessTypeId = businessType.id
                                    selectedBusinessName = businessType.name

                                },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(start = SpacingXXL),
                            text = businessType.name,
                            style = bodyLarge,
                            color = OnBackground
                        )
                        RadioButton(
                            modifier = Modifier.scale(1.3f).padding(end = SpacingXXL),
                            selected = selectedBusinessTypeId == businessType.id,
                            onClick = null,
                            colors = RadioButtonColors(
                                selectedColor = Primary,
                                unselectedColor = Divider,
                                disabledSelectedColor = Divider,
                                disabledUnselectedColor = Divider
                            )
                        )
                    }

                    if(index < pagingItems.itemCount - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SpacingXXL)
                                .height(0.55.dp)
                                .background(Divider.copy(alpha = 0.5f))
                        )
                    }
                }
            }
        }
    }
}