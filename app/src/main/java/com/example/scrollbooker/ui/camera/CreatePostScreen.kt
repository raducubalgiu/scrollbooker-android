package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CreatePostScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val verticalScroll = rememberScrollState()
    var description by rememberSaveable { mutableStateOf("") }

    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val previewHeight = 180.dp

    Scaffold(
        topBar = { Header(onBack=onBack) },
        bottomBar = {
            HorizontalDivider(color = Divider, thickness = 0.55.dp)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding)
                .padding(top = BasePadding, bottom = bottomInset + BasePadding)
            ) {
                MainButton(
                    modifier = Modifier.weight(1f),
                    title = "Draft",
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceBG,
                        contentColor = OnSurfaceBG
                    )
                )
                Spacer(Modifier.width(SpacingS))
                MainButton(
                    modifier = Modifier.weight(1f),
                    title = "Posteaza",
                    onClick = {}
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(verticalScroll)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
        ) {


            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding),
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier
                    .height(previewHeight)
                    .aspectRatio(9f / 12f)
                    .clip(shape = ShapeDefaults.Medium)
                    .background(SurfaceBG)
                    .clickable(onClick = {})
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://media.scrollbooker.ro/frizerie-4.jpg ")
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Post Grid",
                        contentScale = ContentScale.Crop,
                        onError = { Timber.tag("Post Grid Error").e("ERROR: ${it.result.throwable.message}") },
                        modifier = Modifier.matchParentSize()
                    )

                    Box(modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.2f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                )
                            )
                        )
                    )
                }

                Spacer(Modifier.width(SpacingS))

                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .height(previewHeight),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = {
                        Text(
                            text = "Adauga o descriere..",
                            color = Divider
                        )
                    },
                    singleLine = false,
                    minLines = 4,
                    maxLines = 6,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Primary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Divider,
                        unfocusedLabelColor = Color.Transparent,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnBackground,
                        disabledContainerColor = Color.Transparent
                    )
                )
            }
        }
    }
}