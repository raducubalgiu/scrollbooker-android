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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocationPermissionStatus
import com.example.scrollbooker.ui.PrecisionMode
import com.example.scrollbooker.ui.search.components.SearchEnableLocationBanner
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchMap
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit
) {
    val locationController = LocalLocationController.current
    val locationState by locationController.stateFlow.collectAsStateWithLifecycle()
    val markersUiState by viewModel.markersUiState.collectAsState()

    val permissionStatus = locationState.permissionStatus

    val context = LocalContext.current
    val activity = context as Activity

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

    val scope = rememberCoroutineScope()
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
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetAction = SearchSheetActionEnum.NONE
                    }
                },
            )
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var sheetHeaderDp by remember { mutableStateOf(0.dp) }
    val isExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

    val cameraPosition by viewModel.cameraPosition.collectAsState()
    val viewportState = rememberMapViewportState {
        setCameraOptions {
            center(Point.fromLngLat(cameraPosition.longitude, cameraPosition.latitude))
            zoom(cameraPosition.zoom)
            bearing(cameraPosition.bearing)
            pitch(cameraPosition.pitch)
        }
    }

    fun flyToUserLocation() {
        val loc = GeoPoint(
            lat = 44.443697,
            lng = 25.978861
        )
        scope.launch {
            viewportState.flyTo(
                cameraOptions = CameraOptions.Builder()
                    .center(
                        Point.fromLngLat(
                            loc.lng,
                            loc.lat
                        )
                    )
                    .zoom(13.5)
                    .bearing(cameraPosition.bearing)
                    .pitch(cameraPosition.pitch)
                    .build()
            )
        }
    }

    Scaffold(
        topBar = {
            SearchHeader(
                modifier = Modifier.statusBarsPadding(),
                headline = stringResource(R.string.allServices),
                subHeadline = stringResource(R.string.anytimeAnyHour),
                sheetValue = scaffoldState.bottomSheetState.currentValue,
                onClick = { openSheetWith(SearchSheetActionEnum.OPEN_SERVICES) },
                onMapToggle = {
                    scope.launch {
                        if(isExpanded) scaffoldState.bottomSheetState.partialExpand()
                        else scaffoldState.bottomSheetState.expand()
                    }
                }
            )

            when(permissionStatus) {
                LocationPermissionStatus.NOT_DETERMINED,
                LocationPermissionStatus.DENIED_CAN_ASK_AGAIN -> {
                    SearchEnableLocationBanner(
                        modifier = Modifier.statusBarsPadding(),
                        onEnableClick = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        },
                        onCancel = {}
                    )
                }
                else -> Unit
            }
        },
        bottomBar = { BottomBar() }
    ) { padding ->
        SearchMap(
            viewModel = viewModel,
            cameraPosition = cameraPosition,
            viewportState = viewportState,
            markersUiState = markersUiState,
            userLocation = locationState.lastAccurateLocation
        )

        Box(modifier = Modifier
            .padding(
                top = padding.calculateTopPadding() + BasePadding,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            if(markersUiState.isLoading) {
                SearchMapLoading()
            }

            IconButton(
                modifier = Modifier
                    .padding(bottom = padding.calculateBottomPadding())
                    .padding(SpacingXL)
                    .size(52.dp)
                    .align(Alignment.BottomEnd),
                onClick = { flyToUserLocation() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Background,
                    contentColor = OnBackground
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.NearMe,
                    contentDescription = null
                )
            }

            BottomSheetScaffold(
                sheetPeekHeight = sheetHeaderDp,
                scaffoldState = scaffoldState,
                sheetDragHandle = {},
                sheetContainerColor = Background,
                containerColor = Background,
                sheetContent = {
                    SearchSheetHeader(
                        onMeasured = { sheetHeaderDp = it },
                        onAction = openSheetWith
                    )
                    Column(Modifier.fillMaxSize()) {  }
                    //SearchResultsList(dummyBusinesses)
                }
            ) {}
        }
    }
}
