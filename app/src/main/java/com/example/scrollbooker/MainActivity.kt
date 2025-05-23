package com.example.scrollbooker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.nav.RootNavHost
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    @Inject
//    lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = MainViewModel()

        setContent {
            ScrollBookerTheme(dynamicColor = false) {
                //val navController = rememberNavController()
//                var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }
//
//                LaunchedEffect(Unit) {
//                    val token = authDataStore.getAccessToken().first()
//                    isLoggedIn = !token.isNullOrEmpty()
//                }
//                val isLoggedIn = true
//
//                val startDestination = if (isLoggedIn == true) GlobalRoute.MAIN else GlobalRoute.AUTH
//
//                if(isLoggedIn == null) {
//                    Box(modifier = Modifier.fillMaxSize()) {
//                        CircularProgressIndicator()
//                    }
//                } else if(isLoggedIn == true) {
//                    MainScaffold(
//                        navController = navController,
//                        startDestination = startDestination
//                    )
//                } else {
//                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
//                        AppNavHost(
//                            navController = navController,
//                            startDestination = startDestination
//                        )
//                    }
//                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    RootNavHost()
                }
            }
        }
    }
}