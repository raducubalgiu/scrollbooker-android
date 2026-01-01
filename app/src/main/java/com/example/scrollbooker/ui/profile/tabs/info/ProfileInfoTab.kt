package com.example.scrollbooker.ui.profile.tabs.info
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.theme.titleLarge
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.SchedulesSection
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import timber.log.Timber

@Composable
fun ProfileInfoTab(
    viewModel: MyProfileViewModel,
    paddingTop: Dp
) {
    val state by viewModel.about.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = paddingTop)
        .padding(horizontal = BasePadding)
        .verticalScroll(rememberScrollState())
    ) {
        when(val about = state) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.padding(top = 50.dp),
                    arrangement = Arrangement.Top
                )
            }
            is FeatureState.Success -> {
                val data = about.data

                Text(
                    modifier = Modifier.padding(vertical = BasePadding),
                    text = stringResource(R.string.address),
                    fontWeight = FontWeight.SemiBold,
                    style = titleLarge
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_location_outline),
                        contentDescription = null
                    )

                    Spacer(Modifier.width(BasePadding))

                    Text(text = data.address)
                }

                Box(modifier = Modifier
                    .padding(top = BasePadding)
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape = ShapeDefaults.ExtraLarge)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("")
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Business Location Map",
                        contentScale = ContentScale.Crop,
                        onError = { Timber.tag("Business Map Error").e("ERROR: ${it.result.throwable.message}") },
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(modifier = Modifier
                        .fillMaxSize()
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

                Text(
                    modifier = Modifier.padding(
                        top = SpacingXXL,
                        bottom = BasePadding
                    ),
                    text = stringResource(R.string.description),
                    fontWeight = FontWeight.SemiBold,
                    style = titleLarge
                )

                data.description?.let {
                    Text(text = it)
                }

                Text(
                    modifier = Modifier.padding(
                        top = SpacingXXL,
                        bottom = BasePadding
                    ),
                    text = stringResource(R.string.schedule),
                    fontWeight = FontWeight.SemiBold,
                    style = titleLarge
                )

                SchedulesSection(data.schedules)
            }
        }
    }
}