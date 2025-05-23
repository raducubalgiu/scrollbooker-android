package com.example.scrollbooker.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Input
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun LoginScreen(
    //navController: NavController,
    //viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    //val state by viewModel.loginState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 50.dp, horizontal = BasePadding)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.login)
            )
            Text(text = "Please sign in to continue")
            Spacer(Modifier.height(16.dp))

            Input(
                value = username,
                onValueChange = {username = it},
                label = "Username"
            )

            Spacer(Modifier.height(8.dp))

            Input(
                value = password,
                onValueChange = { password = it },
                label = "Password"
            )

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                //onClick = { viewModel.login("radu", "password") },
                onClick = { onLoginSuccess },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = stringResource(id = R.string.login))
            }

//            when(state) {
//                is LoginState.Loading -> CircularProgressIndicator()
//                is LoginState.Success -> {
//                    LaunchedEffect(Unit) {
//                    }
//                }
//                is LoginState.Error -> {
//                    Text((state as LoginState.Error).message, color = Color.Red)
//                }
//                else -> {}
//            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "${stringResource(id = R.string.dontHaveAnAccount)}?"
                )
                TextButton(onClick = {
                    //navController.navigate(route = AuthRoute.Register.route)
                }) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.register)
                    )
                }
            }
        }
    }
}