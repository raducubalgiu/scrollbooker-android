package com.example.scrollbooker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.AppNavHost
import com.example.scrollbooker.core.nav.BottomNavigationBar
import com.example.scrollbooker.core.nav.MainScaffold
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.data.dataStore.AuthDataStore
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ScrollBookerTheme(dynamicColor = false) {
                val navController = rememberNavController()
//                var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
//
//                LaunchedEffect(Unit) {
//                    val token = authDataStore.getAccessToken().first()
//                    isLoggedIn = !token.isNullOrEmpty()
//                }
                val isLoggedIn = true

                val startDestination = if (isLoggedIn == true) GlobalRoute.MAIN else GlobalRoute.AUTH

                if(isLoggedIn == null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                } else if(isLoggedIn == true) {
                    MainScaffold(
                        navController = navController,
                        startDestination = startDestination
                    )
                } else {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                        AppNavHost(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}