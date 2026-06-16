package com.example.scrollbooker.ui.search
import BottomBar
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.navigation.navigators.SearchNavigator
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainList
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchList
import com.example.scrollbooker.ui.search.components.map.SearchMap
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import rememberLocationsCountText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    isSearchTab: Boolean,
    searchNavigate: SearchNavigator,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasLocationPermission = granted
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    DisposableEffect(isSearchTab, hasLocationPermission) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    viewModel.setUserLocation(
                        // Here need to use Double in the future
                        BusinessCoordinates(
                            lat = location.latitude.toFloat(),
                            lng = location.longitude.toFloat()
                        )
                    )
                }
            }
        }

        if (isSearchTab) {
            if (hasLocationPermission) {
                try {
                    val locationRequest = LocationRequest.Builder(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        30000L
                    ).apply {
                        setMinUpdateIntervalMillis(15000L)
                        setMaxUpdateDelayMillis(60000L)
                    }.build()

                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        context.mainLooper
                    )

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            viewModel.setUserLocation(
                                BusinessCoordinates(
                                    // Here need to use Double in the future
                                    lat = it.latitude.toFloat(),
                                    lng = it.longitude.toFloat()
                                )
                            )
                        }
                    }
                } catch (_: SecurityException) {
                    // Tratat silențios în producție sau trimis către un serviciu de crash reporting (ex: Crashlytics)
                }
            } else {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    val isMapMounted by viewModel.isMapMounted.collectAsStateWithLifecycle()

    LaunchedEffect(isSearchTab, isMapMounted) {
        if(isSearchTab && !isMapMounted) {
            viewModel.setMapMounted()
        }
    }


    val businessesSheet = viewModel.sheetPagingFlow.collectAsLazyPagingItems()
    val businessesCount by viewModel.sheetTotalCount.collectAsStateWithLifecycle()
    val markersUiState by viewModel.markersUiState.collectAsStateWithLifecycle()

    val businessesCountText = rememberLocationsCountText(businessesCount)

    val requestState by viewModel.request.collectAsStateWithLifecycle()
    val businessDomains by viewModel.businessDomains.collectAsStateWithLifecycle()
    val selectedServiceDomain by viewModel.selectedServiceDomain.collectAsStateWithLifecycle()
    val servicesState by viewModel.services.collectAsState()
    val selectedServicesText by viewModel.selectedServicesText.collectAsStateWithLifecycle()

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

    Scaffold(
        topBar = {
            SearchHeader(
                modifier = Modifier.statusBarsPadding(),
                activeFiltersCount = requestState.activeFiltersCount(),
                headline = selectedServicesText,
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
                onNavigateToBusinessProfile = { searchNavigate.toBusinessProfile(it) }
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
                        onNavigateToBusinessProfile = { searchNavigate.toBusinessProfile(it) },
                        onSelectProduct = { searchNavigate.toBookingFromProduct(
                            it,
                            source = BookingSourceEnum.SEARCH
                        ) }
                    )
                }
            ) {}
        }
    }
}