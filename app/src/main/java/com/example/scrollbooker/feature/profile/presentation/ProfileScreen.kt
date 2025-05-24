package com.example.scrollbooker.feature.profile.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.BottomSheet
import com.example.scrollbooker.components.ItemList
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var showBottomSheet by remember { mutableStateOf(false) }

    BottomSheet(
        onDismiss = { showBottomSheet = false },
        showBottomSheet = showBottomSheet,
        showHeader = false,
        content = {
            ItemList(
                headLine = stringResource(id = R.string.calendar),
                leftIcon = painterResource(R.drawable.ic_calendar),
                onClick = {}
            )
            ItemList(
                headLine = stringResource(id = R.string.myBusiness),
                leftIcon = painterResource(R.drawable.ic_business),
                onClick = {}
            )
            ItemList(
                headLine = stringResource(id = R.string.settings),
                leftIcon = painterResource(R.drawable.ic_settings),
                onClick = {
                    showBottomSheet = false
                    navController.navigate("settings")
                }
            )
        }
    )

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader(onOpenBottomSheet = { showBottomSheet = true })
    }
}