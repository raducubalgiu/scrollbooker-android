package com.example.scrollbooker.components.customized.post.sheets.deletePost

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingL
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun DeletePostSheet(
    postId: Int,
    onClose: () -> Unit,
    onDeleted: (postId: Int) -> Unit
) {
    val viewModel: DeletePostViewModel = hiltViewModel()
    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event -> snackBarController.show(event) }
    }

    Column {
        SheetHeader(
            title = stringResource(R.string.deletePost),
            onClose = onClose
        )

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            text = stringResource(R.string.areYouSureYouWantDeletePost),
            style = bodyMedium,
            color = OnSurfaceBG
        )

        Spacer(Modifier.height(SpacingL))

        Column(
            modifier = Modifier.padding(horizontal = BasePadding),
            verticalArrangement = Arrangement.spacedBy(SpacingS)
        ) {
            MainButton(
                title = stringResource(R.string.delete),
                isLoading = isDeleting,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error,
                    contentColor = OnError
                ),
                onClick = { viewModel.deletePost(postId, onSuccess = { onDeleted(postId) }) }
            )

            MainButtonOutlined(
                fullWidth = true,
                title = stringResource(R.string.cancel),
                isEnabled = !isDeleting,
                onClick = onClose
            )
        }

        SnackbarHost(hostState = hostState) { data ->
            Snackbar(
                modifier = Modifier.padding(horizontal = BasePadding, vertical = SpacingS),
                snackbarData = data,
                containerColor = Error,
                contentColor = OnError
            )
        }

        Spacer(Modifier.height(SpacingL))
    }
}
