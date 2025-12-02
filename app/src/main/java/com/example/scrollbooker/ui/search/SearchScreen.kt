package com.example.scrollbooker.ui.search
import BottomBar
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.LocalLocationController
import com.example.scrollbooker.ui.LocationPermissionStatus
import com.example.scrollbooker.ui.PrecisionMode
import com.example.scrollbooker.ui.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.ui.search.components.SearchBusinessCard
import com.example.scrollbooker.ui.search.components.SearchEnableLocationBanner
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchLocationIconButton
import com.example.scrollbooker.ui.search.components.SearchMap
import com.example.scrollbooker.ui.search.components.SearchMapLoading
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.components.ServiceUiModel
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium
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
    val isSheetExpanded by viewModel.isSheetExpanded.collectAsState()
    val businessesSheet = viewModel.sheetPagingFlow.collectAsLazyPagingItems()

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

    LaunchedEffect(scaffoldState) {
        snapshotFlow {
            scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded
        }.collect { expanded -> viewModel.setSheetExpanded(expanded) }
    }

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
            val newZoom = 13.5
            val cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(loc.lng, loc.lat))
                .zoom(13.5)
                .bearing(cameraPosition.bearing)
                .pitch(cameraPosition.pitch)
                .build()
            viewportState.flyTo(cameraOptions)
            viewModel.updateCamera(
                cameraPosition.copy(
                    latitude = loc.lat,
                    longitude = loc.lng,
                    zoom = newZoom
                )
            )
        }
    }

    val refreshState = businessesSheet.loadState.refresh
    val appendState = businessesSheet.loadState.append
    val isInitialLoading = refreshState is LoadState.Loading && businessesSheet.itemCount == 0
    //val isRefreshing = refreshState is LoadState.Loading && businessesSheet.itemCount > 0
    val isAppending = appendState is LoadState.Loading

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
                        if(isSheetExpanded) scaffoldState.bottomSheetState.partialExpand()
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

            SearchLocationIconButton(
                paddingBottom = padding.calculateBottomPadding(),
                onClick = { flyToUserLocation() }
            )

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
                    Column(Modifier.fillMaxSize()) {
                        if(isInitialLoading) LoadingScreen()
                        else Box(Modifier.fillMaxSize()) {
                            LazyColumn {
                                items(businessesSheet.itemCount) { index ->
                                    businessesSheet[index]?.let { business ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .padding(horizontal = BasePadding)
                                        ) {

                                            Box(modifier = Modifier
                                                .fillMaxWidth()
                                                .height(250.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                            ) {
                                                AsyncImage(
                                                    model = "",
                                                    contentDescription = null,
                                                    contentScale = ContentScale.FillBounds,
                                                    modifier = Modifier.matchParentSize()
                                                )
                                                Box(modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(
                                                        brush = Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color.Black.copy(alpha = 0.2f),
                                                                Color.Transparent,
                                                                Color.Black.copy(alpha = 0.4f)
                                                            )
                                                        )
                                                    )
                                                )
                                            }

                                            Spacer(Modifier.height(8.dp))

                                            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                                Text(
                                                    text = business.business.fullName,
                                                    style = titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                                    fontSize = 18.sp
                                                )

                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    RatingsStars(
                                                        rating = 4.5f,
                                                        maxRating = 5,
                                                        starSize = 20.dp
                                                    )

                                                    Text(
                                                        text = "${business.business.ratingsAverage}",
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                    Text(
                                                        text = "(${business.business.ratingsCount})",
                                                        color = Color.Gray
                                                    )
                                                }

                                                Text(
                                                    text = business.address,
                                                    color = Color.Gray
                                                )

                                                Spacer(Modifier.height(8.dp))

//                                                    // Servicii
//                                                    services.take(3).forEach { service ->
//                                                        Row(
//                                                            modifier = Modifier.fillMaxWidth(),
//                                                            horizontalArrangement = Arrangement.SpaceBetween
//                                                        ) {
//                                                            Column {
//                                                                Text(service.name, style = bodyMedium)
//                                                                Text(
//                                                                    service.duration,
//                                                                    style = MaterialTheme.typography.bodySmall,
//                                                                    color = Color.Gray
//                                                                )
//                                                            }
//                                                            Text(
//                                                                "${service.price} RON",
//                                                                style = bodyMedium,
//                                                                fontWeight = FontWeight.Bold
//                                                            )
//                                                        }
//                                                        Spacer(Modifier.height(6.dp))
//                                                    }

                                                Text(
                                                    text = "See more",
                                                    style = bodySmall.copy(color = Color(0xFF5E35B1)),
                                                    modifier = Modifier.padding(top = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                if(isAppending) {
                                    item { LoadMoreSpinner() }
                                }
                            }
                        }
                    }
                }
            ) {}
        }
    }
}
