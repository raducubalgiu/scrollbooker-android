package com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.ui.onboarding.business.collectGallery.BusinessPhotoUIState
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun MyBusinessGalleryTab(
    photosState: BusinessPhotoUIState,
    isSaving: FeatureState<Unit>?,
    onSetImage: (Int, Uri) -> Unit,
    onClearImage: (Int) -> Unit,
    onSaveGallery: () -> Unit
) {
    var pendingSlotIndex by rememberSaveable { mutableStateOf<Int?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        val idx = pendingSlotIndex
        if(uri != null && idx != null) {
            onSetImage(idx, uri)
        }
        pendingSlotIndex = null
    }

    val isLoading = isSaving == FeatureState.Loading
    val hasPhotos = photosState.images.any { it != null }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(SpacingS),
            contentPadding = PaddingValues(BasePadding)
        ) {
            items(5) { i ->
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
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .shadow(elevation = 6.dp, shape = CircleShape, clip = false),
                            onClick = { onClearImage(i) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close_circle_solid),
                                contentDescription = "Remove Image",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainButton(
                modifier = Modifier.padding(BasePadding),
                onClick = onSaveGallery,
                isLoading = isLoading,
                enabled = !isLoading && hasPhotos && photosState.hasChanges,
                title = stringResource(R.string.save)
            )
        }
    }
}