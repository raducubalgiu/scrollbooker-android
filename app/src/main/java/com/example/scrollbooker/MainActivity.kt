package com.example.scrollbooker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.LocalRootNavController
import com.example.scrollbooker.core.nav.RootNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val rootNavController = rememberNavController()

            CompositionLocalProvider(LocalRootNavController provides rootNavController) {
                RootNavHost(navController = rootNavController)
            }
        }
    }
}