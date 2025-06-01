package com.example.scrollbooker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.LocalRootNavController
import com.example.scrollbooker.core.nav.RootNavHost
import com.example.scrollbooker.feature.auth.presentation.AuthViewModel
import com.example.scrollbooker.feature.auth.presentation.FeatureState
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
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
        enableEdgeToEdge()

        setContent {
            val rootNavController = rememberNavController()

            CompositionLocalProvider(LocalRootNavController provides rootNavController) {
                ScrollBookerTheme {
                    RootNavHost(
                        navController = rootNavController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}