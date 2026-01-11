package com.example.scrollbooker.ui.profile.edit
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R

@Composable
fun EditAvatarCropScreen(
    viewModel: MyProfileViewModel,
    onBack: () -> Unit
) {
    val photoUri by viewModel.photoUri.collectAsState()

    Scaffold(topBar = { Header(onBack = onBack) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Avatar(
                        url = photoUri.toString(),
                        size = 300.dp
                    )
                }
            }

            MainButton(
                modifier = Modifier.padding(BasePadding),
                onClick = {
                    photoUri?.let { viewModel.updateAvatar(it)  }
                },
                title = stringResource(R.string.save)
            )
        }
    }
}