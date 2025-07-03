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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.screens.profile.myBusiness.myBusinessLocation.MyBusinessLocationViewModel

@Composable
fun CollectBusinessTypeScreen(
    viewModel: MyBusinessLocationViewModel,
    onNext: () -> Unit
) {
    val pagingItems = viewModel.businessTypes.collectAsLazyPagingItems()
    val selectedBusinessType by viewModel.selectedBusinessType.collectAsState()

    FormLayout(
        enableBack = false,
        isEnabled = selectedBusinessType != null,
        headLine = stringResource(id = R.string.collectBusinessTypeHeadline),
        subHeadLine = stringResource(id = R.string.collectBusinessTypeSubHeadline),
        buttonTitle = stringResource(R.string.nextStep),
        onNext = onNext,
    ) {
        pagingItems.apply {
            when(loadState.refresh) {
                is LoadState.Loading -> LoadingScreen()
                is LoadState.Error -> ErrorScreen()
                is LoadState.NotLoading -> Unit
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
                    val selected = selectedBusinessType?.id == businessType.id

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .background(Background)
                            .selectable(
                                selected = selected,
                                onClick = { viewModel.setBusinessType(businessType) },
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
                            selected = selected,
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