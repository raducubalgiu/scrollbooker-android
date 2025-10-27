package com.example.scrollbooker.ui.search
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.search.components.MapSearch
import com.example.scrollbooker.ui.search.components.SearchHeader
import com.example.scrollbooker.ui.search.components.SearchResultsList
import com.example.scrollbooker.ui.search.components.SearchSheetHeader
import com.example.scrollbooker.ui.search.sheets.SearchSheetActionEnum
import com.example.scrollbooker.ui.search.sheets.SearchSheets
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

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
        MapSearch(viewModel)

        Box(Modifier.padding(
            top = padding.calculateTopPadding() + BasePadding,
            bottom = padding.calculateBottomPadding()
        )) {
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
                    SearchResultsList()
                }
            ) {}
        }
    }
}

