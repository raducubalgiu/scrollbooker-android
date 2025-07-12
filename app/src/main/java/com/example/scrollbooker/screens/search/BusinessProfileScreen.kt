package com.example.scrollbooker.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun BusinessProfileScreen() {
    val sections = listOf("Despre", "Servicii", "Angajati", "Recenzii")
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val sectionMap = remember {
        mapOf(
            "Despre" to "about",
            "Servicii" to "services",
            "Angajati" to "employees",
            "Recenzii" to "reviews"
        )
    }

    val itemKeys = sectionMap.values.toList()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().statusBarsPadding()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Surface(tonalElevation = 4.dp) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier
                            .fillMaxWidth(),
                        containerColor = Background
                    ) {
                        sections.forEachIndexed { index, label ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = {
                                    coroutineScope.launch {
                                        val targetKey = itemKeys[index]

                                        Timber.tag("Scrollable Business").e("TARGET KEY!!! $targetKey")

                                        val targetIndex = lazyListState.layoutInfo
                                            .visibleItemsInfo
                                            .find { it.key == targetKey }
                                            ?.index
                                            ?: (itemKeys.indexOf(targetKey) + 1)

                                        Timber.tag("Scrollable Business").e("TARGET INDEX!!! $targetKey")
                                        lazyListState.animateScrollToItem(targetIndex)

                                        selectedTabIndex = index
                                    }
                                },
                                text = { Text(label) }
                            )
                        }
                    }
                }
            }

            item(key = "about") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                    Text(text = "About section")
                }
            }

            item(key = "services") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                    Text(text = "Services section")
                }
            }

            item(key = "employees") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                    Text(text = "Employees section")
                }
            }

            item(key = "reviews") {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                    Text(text = "Recenzii section")
                }
            }
        }

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    //Timber.tag("Scrollable Business").e("INDEX!!!! $index")

                    val visibleSections = visibleItems.filter { item ->
                        item.key in itemKeys
                    }

                    val topMostSection = visibleSections.minByOrNull { it.offset }

                    val newIndex = topMostSection?.key?.let { key ->
                        itemKeys.indexOf(key)
                    }

                    if(newIndex != null && newIndex != selectedTabIndex) {
                        selectedTabIndex = newIndex
                    }

//                    val firstRealItem = visibleItems.firstOrNull() { it.key in sectionKeys }
//                    val key = firstRealItem?.key as? String
//                    val newIndex = sectionKeys.indexOf(key)
//                    if(newIndex != -1 && newIndex != selectedTabIndex) {
//                        selectedTabIndex = new
//                    }
                }
        }
    }
}