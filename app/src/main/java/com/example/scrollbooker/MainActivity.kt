package com.example.scrollbooker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.LocalRootNavController
import com.example.scrollbooker.core.nav.RootNavHost
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        val viewModel by viewModels<AuthViewModel>()

        splashScreen.setKeepOnScreenCondition {
            viewModel.loginState.value is FeatureState.Loading
        }

        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()

        setContent {
            val rootNavController = rememberNavController()

            CompositionLocalProvider(LocalRootNavController provides rootNavController) {
                ScrollBookerTheme {
                    Surface(Modifier.fillMaxSize().background(Background)) {
                        RootNavHost(
                            navController = rootNavController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}