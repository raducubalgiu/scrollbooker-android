package com.example.scrollbooker.ui.onboarding.business
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.ui.myBusiness.myBusinessLocation.MyBusinessLocationViewModel
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineSmall
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi
import com.example.scrollbooker.components.customized.BusinessMediaLauncher
import com.example.scrollbooker.components.customized.BusinessMediaTypeEnum

@OptIn(UnstableApi::class)
@Composable
fun CollectBusinessGalleryScreen(
    viewModel: MyBusinessLocationViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val verticalScroll = rememberScrollState()
    val photosState by viewModel.photosState.collectAsState()
    val videoUri by viewModel.videoState.collectAsState()

    var pendingSlotIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    val pickVideo = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { url: Uri? ->
        viewModel.setVideo(url)
    }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val idx = pendingSlotIndex
        if(uri != null && idx != null) {
            viewModel.setImage(idx, uri)
        }
        pendingSlotIndex = null
    }

    FormLayout(
        headLine = stringResource(R.string.visualPresentation),
        subHeadLine = stringResource(R.string.photoGalleryDescription),
        buttonTitle = stringResource(R.string.nextStep),
        onBack = onBack,
        onNext = onNext
    ) {
        Column(modifier = Modifier
            .padding(horizontal = SpacingXXL)
            .verticalScroll(verticalScroll)
        ) {
            BusinessMediaTitle(
                title = stringResource(R.string.video),
                description = stringResource(R.string.addVideoDescription)
            )

            BusinessMediaLauncher(
                type = BusinessMediaTypeEnum.VIDEO,
                uri = videoUri,
                onClick = {
                    pickVideo.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                    )
                },
                onClear = { viewModel.clearVideo() }
            )

            BusinessMediaTitle(
                title = "${stringResource(R.string.images)}*",
                description = stringResource(R.string.addImagesDescription)
            )

            repeat(5) { i ->
                BusinessMediaLauncher(
                    type = BusinessMediaTypeEnum.PHOTO,
                    uri = photosState.images[i],
                    onClick = {
                        pendingSlotIndex = i
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onClear = {
                        viewModel.clearImage(i)
                    }
                )
            }
        }
    }
}

@Composable
private fun BusinessMediaTitle(
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpacingS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            style = bodyLarge,
            fontWeight = FontWeight.Bold,
            text = "\u2022",
            fontSize = 25.sp
        )
        Spacer(Modifier.width(BasePadding))
        Text(
            style = headlineSmall,
            fontWeight = FontWeight.Bold,
            text = title
        )
    }

    Spacer(Modifier.height(SpacingS))

    Text(
        color = Color.Gray,
        text = description
    )

    Spacer(Modifier.height(SpacingS))
}