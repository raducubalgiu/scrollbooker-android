package com.example.scrollbooker.ui.search
import BottomBar
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainList
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchList
import com.example.scrollbooker.ui.search.components.map.SearchMap
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheetUser
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch
import rememberLocationsCountText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    isSearchTab: Boolean,
    onNavigateToBusinessProfile: (Int) -> Unit
) {
    val isSystemInDarkMode = isSystemInDarkTheme()
    val isMapMounted by viewModel.isMapMounted.collectAsStateWithLifecycle()

    LaunchedEffect(isSearchTab, isMapMounted) {
        if(isSearchTab && !isMapMounted) {
            viewModel.setMapMounted()
        }
    }

    val scope = rememberCoroutineScope()

    val businessesSheet = viewModel.sheetPagingFlow.collectAsLazyPagingItems()
    val businessesCount by viewModel.sheetTotalCount.collectAsStateWithLifecycle()
    val markersUiState by viewModel.markersUiState.collectAsStateWithLifecycle()

    val businessesCountText = rememberLocationsCountText(businessesCount)

    val requestState by viewModel.request.collectAsStateWithLifecycle()
    val businessDomains by viewModel.businessDomains.collectAsStateWithLifecycle()
    val selectedServiceDomain by viewModel.selectedServiceDomain.collectAsStateWithLifecycle()
    val servicesState by viewModel.services.collectAsState()

    val services = (servicesState as? FeatureState.Success)?.data
    val isLoadingServices = servicesState is FeatureState.Loading

    val listState = rememberLazyListState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetAction by rememberSaveable { mutableStateOf(SearchSheetActionEnum.NONE) }
    val latestSheetState by rememberUpdatedState(sheetState)

    val openSheetWith: (SearchSheetActionEnum) -> Unit = remember {
        { action ->
            sheetAction = action
            scope.launch { latestSheetState.show() }
        }
    }

    if(sheetState.isVisible) {
        key(sheetAction) {
            SearchSheets(
                sheetState = sheetState,
                viewModel = viewModel,
                requestState = requestState,
                businessDomains = businessDomains,
                selectedServiceDomain = selectedServiceDomain,
                services = services,
                isLoadingServices = isLoadingServices,
                sheetAction = sheetAction,
                onClose = {
                    scope.launch { listState.scrollToItem(0) }
                    sheetAction = SearchSheetActionEnum.NONE
                },
            )
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var sheetHeaderDp by remember { mutableStateOf(0.dp) }

    val isMapReady by viewModel.isMapReady.collectAsState()
    val isStyleLoaded by viewModel.isStyleLoaded.collectAsState()

    val isMapLoading = !isMapReady || !isStyleLoaded

    val refreshState = businessesSheet.loadState.refresh
    val appendState = businessesSheet.loadState.append

    val isInitialLoading = refreshState is LoadState.Loading && businessesSheet.itemCount == 0
    val isRefreshing = refreshState is LoadState.Loading && businessesSheet.itemCount > 0

    val bookingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedBusinessOwner by viewModel.selectedBusinessOwner.collectAsState()

    if(bookingsSheetState.isVisible) {
        selectedBusinessOwner?.let { owner ->
            Sheet(
                modifier = Modifier.statusBarsPadding(),
                sheetState = bookingsSheetState,
                onClose = {
                    scope.launch {
                        viewModel.clearBusinessOwner()
                        bookingsSheetState.hide()
                    }
                }
            ) {
                BookingsSheet(
                    user = BookingsSheetUser(
                        id = owner.id,
                        username = owner.username,
                        fullName = owner.fullName,
                        avatar = owner.avatar,
                        profession = owner.profession,
                        ratingsCount = owner.ratingsCount,
                        ratingsAverage = owner.ratingsAverage
                    ),
                    onClose = { scope.launch { bookingsSheetState.hide() } }
                )
            }
        }
    }

    Scaffold(
        topBar = {
            SearchHeader(
                modifier = Modifier.statusBarsPadding(),
                activeFiltersCount = requestState.activeFiltersCount(),
                headline = stringResource(R.string.allServices),
                subHeadline = requestState.filters.dateTimeSummary(),
                onClick = { openSheetWith(SearchSheetActionEnum.OPEN_SERVICES) },
                onFilter = { openSheetWith(SearchSheetActionEnum.OPEN_FILTERS) }
            )
        },
        bottomBar = { BottomBar() }
    ) { padding ->
        val paddingBottom = padding.calculateBottomPadding() + sheetHeaderDp + BasePadding

        if(isMapMounted) {
            SearchMap(
                viewModel = viewModel,
                markersUiState = markersUiState,
                userLocation = null,
                isMapLoading = isMapLoading,
                onSheetExpand = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                paddingBottom = paddingBottom,
                onNavigateToBusinessProfile = onNavigateToBusinessProfile
            )
        }

        Box(modifier = Modifier
            .padding(
                top = padding.calculateTopPadding() + BasePadding,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBusinessDomainList(
                    businessDomains = businessDomains,
                    selectedBusinessDomain = requestState.filters.businessDomainId,
                    onClick = {
                        scope.launch { listState.scrollToItem(0) }
                        viewModel.setBusinessDomain(it)
                    }
                )

                if(markersUiState.isLoading) {
                    SearchMapLoading()
                }
            }
            
            BottomSheetScaffold(
                sheetPeekHeight = sheetHeaderDp,
                scaffoldState = scaffoldState,
                sheetDragHandle = {},
                sheetShadowElevation = 6.dp,
                sheetContainerColor = Background,
                containerColor = Background,
                sheetContent = {
                    SearchSheetHeader(
                        onMeasured = { sheetHeaderDp = it },
                        isLoading = isInitialLoading || isRefreshing || isMapLoading,
                        businessesCountText = businessesCountText
                    )

                    SearchList(
                        appendState = appendState,
                        listState = listState,
                        businessesSheet = businessesSheet,
                        onNavigateToBusinessProfile = onNavigateToBusinessProfile,
                        onOpenBookingsSheet = {
                            scope.launch {
                                viewModel.setBusinessOwner(it)
                                bookingsSheetState.show()
                            }
                        }
                    )
                }
            ) {}
        }
    }
}

//val locationController = LocalLocationController.current
//val locationState by locationController.stateFlow.collectAsStateWithLifecycle()
//val context = LocalContext.current
//val activity = context as Activity
//val permissionStatus = locationState.permissionStatus

//val permissionLauncher = rememberLauncherForActivityResult(
//    contract = ActivityResultContracts.RequestPermission()
//) { granted ->
//    val canAskAgain = ActivityCompat.shouldShowRequestPermissionRationale(
//        activity,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    )
//
//    locationController.onPermissionResult(granted, canAskAgain)
//}
//
//LaunchedEffect(Unit) {
//    val isGranted = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    locationController.syncInitialPermission(isGranted)
//}
//
//LaunchedEffect(permissionStatus) {
//    when(permissionStatus) {
//        LocationPermissionStatus.GRANTED -> locationController.startUpdates(PrecisionMode.BALANCED)
//        LocationPermissionStatus.DENIED_PERMANENTLY -> locationController.stopUpdates()
//        else -> Unit
//    }
//}
//
//DisposableEffect(Unit) {
//    onDispose { locationController.stopUpdates() }
//}
