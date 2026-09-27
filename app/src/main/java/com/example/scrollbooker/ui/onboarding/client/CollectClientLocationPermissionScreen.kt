package com.example.scrollbooker.ui.onboarding.client

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun CollectClientLocationPermissionScreen(
    viewModel: CollectClientLocationPermissionViewModel,
    onNext: () -> Unit
) {
    val isSaving by viewModel.isSaving.collectAsState()
    val isLoading = isSaving is FeatureState.Loading

    // The OS permission result isn't read here - the step advances either way (granted or
    // denied). Granting still matters: it's what lets UserLocationService return a real fix
    // later, for screens that read location live (LinkedProducts, Search, business profile).
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> onNext() }

    FormLayout(
        isEnabled = !isLoading,
        isLoading = isLoading,
        headLine = stringResource(R.string.locationPermission),
        subHeadLine = stringResource(R.string.locationPermissionDescription),
        buttonTitle = stringResource(R.string.allowLocationAccess),
        onBack = {},
        onNext = { permissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(BasePadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(90.dp)
                )
            }

            TextButton(
                enabled = !isLoading,
                onClick = onNext
            ) {
                Text(
                    text = stringResource(R.string.notNow),
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
    }
}
