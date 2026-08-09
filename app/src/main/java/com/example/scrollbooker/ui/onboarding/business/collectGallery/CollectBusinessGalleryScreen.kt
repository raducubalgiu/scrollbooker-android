package com.example.scrollbooker.ui.onboarding.business.collectGallery
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

@OptIn(UnstableApi::class)
@Composable
fun CollectBusinessGalleryScreen(
    viewModel: CollectBusinessGalleryViewModel,
    onBack: () -> Unit,
    onNext: (Boolean) -> Unit
) {
    val verticalScroll = rememberScrollState()
    val photosState by viewModel.photosState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsState()

    var pendingSlotIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val idx = pendingSlotIndex
        if(uri != null && idx != null) {
            viewModel.setImage(idx, uri)
        }
        pendingSlotIndex = null
    }

    val isLoading = isSaving == FeatureState.Loading
    val hasPhotos = photosState.images.any { it != null }

    FormLayout(
        headLine = stringResource(R.string.visualPresentation),
        subHeadLine = stringResource(R.string.photoGalleryDescription),
        buttonTitle = stringResource(R.string.nextStep),
        onBack = onBack,
        onNext = { onNext(!hasPhotos) },
        isEnabled = !isLoading,
        isLoading = isLoading
    ) {
        Column(modifier = Modifier
            .padding(horizontal = BasePadding)
            .verticalScroll(verticalScroll)
        ) {
            if (!hasPhotos) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = SpacingM),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(BasePadding)
                ) {
                    Text(
                        modifier = Modifier.padding(BasePadding),
                        text = "💡 ${stringResource(R.string.skipUpdateBusinessGalleryMessage)}.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            repeat(5) { i ->
                val uri = photosState.images[i]

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(BasePadding))
                    .background(SurfaceBG)
                    .clickable {
                        pendingSlotIndex = i
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if(uri == null) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Add",
                            tint = Divider
                        )
                    } else {
                        AsyncImage(
                            model = uri,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        IconButton(
                            modifier = Modifier.align(Alignment.TopEnd),
                            onClick = { viewModel.clearImage(i) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close_circle_solid),
                                contentDescription = "Remove Image",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(SpacingS))
            }
        }
    }
}