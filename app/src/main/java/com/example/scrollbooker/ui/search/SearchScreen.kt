package com.example.scrollbooker.ui.search
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.components.SearchBusinessCardModel
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchMap
import com.example.scrollbooker.ui.search.components.SearchResultsList
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.components.markers.BusinessCategory
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import kotlinx.coroutines.launch

data class BusinessAnnotation(
    val longitude: Float,
    val latitude: Float,
    val businessCategory: BusinessCategory
)

val dummyAnnotation = listOf(
    BusinessAnnotation(
        longitude = 26.005958f,
        latitude = 44.450390f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.130997f,
        latitude = 44.437728f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.001870f,
        latitude = 44.419810f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.184851f,
        latitude = 44.416750f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.068575f,
        latitude = 44.458698f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.062456f,
        latitude = 44.476167f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.127325f,
        latitude = 44.372584f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.063068f,
        latitude = 44.494941f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.099786f,
        latitude = 44.444282f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.092442f,
        latitude = 44.456951f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.113862f,
        latitude = 44.456514f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.078979f,
        latitude = 44.453893f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.087547f,
        latitude = 44.416750f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.087547f,
        latitude = 44.408007f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.082024f,
        latitude = 44.431392f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.123331f,
        latitude = 44.435577f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.109896f,
        latitude = 44.433944f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.089314f,
        latitude = 44.427718f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.114041f,
        latitude = 44.442006f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.085455f,
        latitude = 44.441496f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.117741f,
        latitude = 44.421818f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.056729f,
        latitude = 44.445849f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.149659f,
        latitude = 44.404447f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.141191f,
        latitude = 44.426625f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.044787f,
        latitude = 44.421973f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.110142f,
        latitude = 44.448484f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.089133f,
        latitude = 44.443433f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.095167f,
        latitude = 44.455788f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.082904f,
        latitude = 44.438460f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.126651f,
        latitude = 44.414235f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.115899f,
        latitude = 44.428136f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.046757f,
        latitude = 44.442299f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.073821f,
        latitude = 44.456458f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.079567f,
        latitude = 44.411189f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.126095f,
        latitude = 44.444416f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.049352f,
        latitude = 44.437269f,
        businessCategory = BusinessCategory.MEDICAL
    ),
    BusinessAnnotation(
        longitude = 26.131285f,
        latitude = 44.434622f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.064923f,
        latitude = 44.431578f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.092358f,
        latitude = 44.396887f,
        businessCategory = BusinessCategory.AUTO
    ),
    BusinessAnnotation(
        longitude = 26.037674f,
        latitude = 44.430122f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.072894f,
        latitude = 44.460559f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.095509f,
        latitude = 44.46175f,
        businessCategory = BusinessCategory.BEAUTY
    ),
    BusinessAnnotation(
        longitude = 26.071782f,
        latitude = 44.430519f,
        businessCategory = BusinessCategory.BEAUTY
    ),
)

val dummyBusinesses = listOf(
    SearchBusinessCardModel(
        id=1,
        title="Video 1",
        url = "https://media.scrollbooker.ro/business-video-1.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-1-cover.jpeg",
        longitude = 26.005958f,
        latitude = 44.450390f
    ),
    SearchBusinessCardModel(
        id=2, title="Video 2",
        url = "https://media.scrollbooker.ro/business-video-2.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-2-cover.jpeg",
        longitude = 26.130997f,
        latitude = 44.437728f
    ),
    SearchBusinessCardModel(
        id=3, title="Video 3",
        url = "https://media.scrollbooker.ro/business-video-3.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-3-cover.jpeg",
        longitude = 26.001870f,
        latitude = 44.419810f
    ),
    SearchBusinessCardModel(
        id=4, title="Video 4",
        url = "https://media.scrollbooker.ro/business-video-4.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-4-cover.jpeg",
        longitude = 26.184851f,
        latitude = 44.416750f
    ),
    SearchBusinessCardModel(
        id=5,
        title="Video 5",
        url = "https://media.scrollbooker.ro/business-video-5.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-5-cover.jpeg",
        longitude = 26.068575f,
        latitude = 44.458698f
    ),
    SearchBusinessCardModel(
        id=6,
        title="Video 6",
        url = "https://media.scrollbooker.ro/business-video-6.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-6-cover.jpeg",
        longitude = 26.062456f,
        latitude = 44.476167f
    ),
    SearchBusinessCardModel(
        id=7,
        title="Video 7",
        url = "https://media.scrollbooker.ro/business-video-7.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-7-cover.jpeg",
        longitude = 26.127325f,
        latitude = 44.372584f
    ),
    SearchBusinessCardModel(
        id=8, title="Video 8",
        url = "https://media.scrollbooker.ro/business-video-8.mp4",
        coverUrl = "https://media.scrollbooker.ro/business-video-8-cover.jpeg",
        longitude = 26.063068f,
        latitude = 44.494941f
    ),
)

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToBusinessProfile: () -> Unit
) {
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
        },
        bottomBar = { BottomBar() }
    ) { padding ->
        SearchMap(
            viewModel = viewModel,
            dummyAnnotations = dummyAnnotation
        )

        Box(modifier = Modifier
            .padding(
                top = padding.calculateTopPadding() + BasePadding,
                bottom = padding.calculateBottomPadding()
            )
        ) {
            //SearchMapLoading()

//            IconButton(
//                modifier = Modifier
//                    .padding(bottom = padding.calculateBottomPadding())
//                    .padding(SpacingXL)
//                    .size(52.dp)
//                    .align(Alignment.BottomEnd),
//                onClick = {},
//                colors = IconButtonDefaults.iconButtonColors(
//                    containerColor = Background,
//                    contentColor = OnBackground
//                )
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.NearMe,
//                    contentDescription = null
//                )
//            }

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
                    SearchResultsList(dummyBusinesses)
                }
            ) {}
        }
    }
}

