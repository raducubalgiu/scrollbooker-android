package com.example.scrollbooker.ui.search
import BottomBar
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocationPermissionStatus
import com.example.scrollbooker.ui.PrecisionMode
import com.example.scrollbooker.ui.search.components.SearchBusinessDomainList
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchList
import com.example.scrollbooker.ui.search.components.map.SearchMap
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch
import rememberLocationsCountText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as Activity

    val locationController = LocalLocationController.current
    val locationState by locationController.stateFlow.collectAsStateWithLifecycle()
    val permissionStatus = locationState.permissionStatus

    val businessesSheet = viewModel.sheetPagingFlow.collectAsLazyPagingItems()
    val businessesCount by viewModel.sheetTotalCount.collectAsState()
    val businessesCountText = rememberLocationsCountText(businessesCount)

    val businessDomains by viewModel.businessDomains.collectAsState()
    val markersUiState by viewModel.markersUiState.collectAsState()
    val state by viewModel.request.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        val canAskAgain = ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        locationController.onPermissionResult(granted, canAskAgain)
    }

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        locationController.syncInitialPermission(isGranted)
    }

    LaunchedEffect(permissionStatus) {
        when(permissionStatus) {
            LocationPermissionStatus.GRANTED -> locationController.startUpdates(PrecisionMode.BALANCED)
            LocationPermissionStatus.DENIED_PERMANENTLY -> locationController.stopUpdates()
            else -> Unit
        }
    }

    DisposableEffect(Unit) {
        onDispose { locationController.stopUpdates() }
    }

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
                viewModel = viewModel,
                sheetState = sheetState,
                sheetAction = sheetAction,
                onClose = { sheetAction = SearchSheetActionEnum.NONE },
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
                activeFiltersCount = state.activeFiltersCount(),
                headline = stringResource(R.string.allServices),
                subHeadline = stringResource(R.string.anytimeAnyHour),
                onClick = { openSheetWith(SearchSheetActionEnum.OPEN_SERVICES) },
                onFilter = { openSheetWith(SearchSheetActionEnum.OPEN_FILTERS) }
            )
        },
        bottomBar = { BottomBar() }
    ) { padding ->
        val paddingBottom = padding.calculateBottomPadding() + sheetHeaderDp + BasePadding

        SearchMap(
            viewModel = viewModel,
            markersUiState = markersUiState,
            userLocation = locationState.lastAccurateLocation,
            isMapLoading = isMapLoading,
            onSheetExpand = { scope.launch { scaffoldState.bottomSheetState.expand() } },
            paddingBottom = paddingBottom,
            onNavigateToBusinessProfile = onNavigateToBusinessProfile
        )

        Box(modifier = Modifier
            .padding(
                top = padding.calculateTopPadding() + BasePadding,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBusinessDomainList(
                    businessDomains = businessDomains,
                    selectedBusinessDomain = state.filters.businessDomainId,
                    onClick = { viewModel.setBusinessDomain(it) }
                )

                if(markersUiState.isLoading) {
                    SearchMapLoading()
                }
            }
            
            BottomSheetScaffold(
                sheetPeekHeight = sheetHeaderDp,
                scaffoldState = scaffoldState,
                sheetDragHandle = {},
                sheetContainerColor = Background,
                sheetShadowElevation = 6.dp,
                containerColor = Background,
                sheetContent = {
                    SearchSheetHeader(
                        onMeasured = { sheetHeaderDp = it },
                        isLoading = isInitialLoading || isRefreshing || !isMapReady || !isStyleLoaded,
                        businessesCountText = businessesCountText
                    )

                    SearchList(
                        isInitialLoading = isInitialLoading,
                        appendState = appendState,
                        businessesSheet = businessesSheet,
                        onNavigateToBusinessProfile = onNavigateToBusinessProfile
                    )
                }
            ) {}
        }
    }
}
