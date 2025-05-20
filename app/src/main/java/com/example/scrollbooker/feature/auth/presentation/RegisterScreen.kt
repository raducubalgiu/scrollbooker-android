package com.example.scrollbooker.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Input

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(vertical = 100.dp, horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.ExtraBold,
                text = stringResource(id = R.string.register)
            )
            Text(text = "Please sign in to continue")

            Spacer(Modifier.height(16.dp))

            Input(
                value = email,
                onValueChange = { email = it },
                label = "Email"
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
                onClick = { viewModel.login("radu", "password") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = stringResource(id = R.string.register))
            }

            when(state) {
                is RegisterState.Loading -> CircularProgressIndicator()
                is RegisterState.Success -> {
                    LaunchedEffect(Unit) {
                        onRegisterSuccess()
                    }
                }
                is RegisterState.Error -> {
                    Text((state as RegisterState.Error).message, color = Color.Red)
                }
                else -> {}
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onBackground,
                    text = "${stringResource(id = R.string.alreadyHaveAnAccount)}?"
                )

                TextButton(onClick = { navController.navigate(route = "login")  }) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(id = R.string.login)
                    )
                }
            }
        }
    }
}