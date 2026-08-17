package com.example.scrollbooker.ui.myBusiness.myProducts.productState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.myBusiness.myProducts.MyProductsViewModel
import com.example.scrollbooker.ui.theme.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: EditProductViewModel,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val serviceDomains by viewModel.selectedServices.collectAsStateWithLifecycle()
    val loadingProductState by viewModel.loadingProductState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    var showErrors by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.createSuccessEvent.collect {
            myProductsViewModel.refreshProducts()
            onBack()
        }
    }


    Scaffold(
        topBar = {
            Header(
                onBack = onBack,
                title = stringResource(R.string.editService)
            )
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                Spacer(Modifier.height(SpacingM))
                MainButton(
                    modifier = Modifier
                        .padding(horizontal = BasePadding)
                        .navigationBarsPadding(),
                    title = stringResource(R.string.save),
                    isLoading = isSaving,
                    enabled = !isSaving,
                    onClick = {
                        showErrors = true
                        viewModel.editProduct()
                    },
                )
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when (loadingProductState) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                else -> ProductFormContent(
                    viewModel = viewModel,
                    serviceDomains = serviceDomains,
                    showErrors = showErrors,
                    scrollState = scrollState,
                    sheetState = sheetState
                )
            }
        }
    }
}
