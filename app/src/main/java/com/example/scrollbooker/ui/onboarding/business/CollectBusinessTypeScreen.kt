package com.example.scrollbooker.ui.onboarding.business
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.components.core.inputs.SearchBar
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.MyBusinessLocationViewModel

@Composable
fun CollectBusinessTypeScreen(
    viewModel: MyBusinessLocationViewModel,
    onNext: () -> Unit
) {
    val pagingItems = viewModel.businessTypes.collectAsLazyPagingItems()
    val selectedBusinessType by viewModel.selectedBusinessType.collectAsState()

    FormLayout(
        modifier = Modifier.padding(top = 50.dp),
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

                    InputRadio(
                        selected = selected,
                        onSelect = { viewModel.setBusinessType(businessType) },
                        headLine = businessType.name
                    )

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