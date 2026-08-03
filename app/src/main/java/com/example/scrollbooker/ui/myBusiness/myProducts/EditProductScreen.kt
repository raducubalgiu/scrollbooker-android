package com.example.scrollbooker.ui.myBusiness.myProducts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    myProductsViewModel: MyProductsViewModel,
    viewModel: EditProductsViewModel,
    onBack: () -> Unit
) {
//    val context = LocalContext.current
//    val focusManager = LocalFocusManager.current
//    val scrollState = rememberScrollState()
//
//    //val serviceDomains by myProductsViewModel.serviceDomains.collectAsState()
//    val productState by viewModel.productState.collectAsState()
//    val selectedFilters by viewModel.selectedFilters.collectAsState()
//    val isSaving by viewModel.isSaving.collectAsState()
//    val loadingState by viewModel.loadingState.collectAsState()
//
//    val filtersActions = rememberFiltersSectionActions(viewModel)
//    val productInputsActions = rememberProductInputsActions(viewModel)
//
//    var showErrors by rememberSaveable { mutableStateOf(false) }
//    val validation by remember(productState, showErrors, context) {
//        derivedStateOf {
//            if(!showErrors) AddProductValidation(isValid = true)
//            else productState.validate(context)
//        }
//    }
//
////    LaunchedEffect(Unit) {
////        viewModel.editSuccessEvent.collect {
////            myProductsViewModel.refreshCurrentProductSections()
////            onBack()
////        }
////    }
//
//    Scaffold(
//        topBar = {
//            Header(
//                onBack=onBack,
//                title = stringResource(R.string.addNewProduct)
//            )
//        }
//    ) { innerPadding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .clickable(
//                    indication = null,
//                    interactionSource = remember { MutableInteractionSource() }
//                ) { focusManager.clearFocus() }
//        ) {
//            when(loadingState) {
//                is FeatureState.Loading -> LoadingScreen()
//                is FeatureState.Error -> ErrorScreen()
//                else -> {
//                    Column(modifier = Modifier.fillMaxSize()) {
//                        Column(modifier = Modifier
//                            .weight(1f)
//                            .verticalScroll(scrollState)
//                            .padding(horizontal = BasePadding)
//                        ) {
////                            ProductInputs(
////                                state = ProductInputsState(
////                                    showErrors = showErrors,
////                                    validation = validation,
////                                    filtersActions = filtersActions,
////                                    productState = productState,
////                                    serviceDomains = serviceDomains,
////                                    selectedFilters = selectedFilters
////                                ),
////                                actions = productInputsActions,
////                            )
//                        }
//
//                        Column {
//                            HorizontalDivider(color = Divider, thickness = 0.5.dp)
//
//                            MainButton(
//                                modifier = Modifier.padding(BasePadding),
//                                isLoading = isSaving,
//                                enabled = !isSaving,
//                                title = stringResource(R.string.save),
//                                onClick = {
//                                    showErrors = true
//
//                                    if(validation.isValid) {
//                                        viewModel.editProduct()
//                                    }
//                                },
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
}
