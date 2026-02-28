package com.example.scrollbooker.components.customized

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import timber.log.Timber

@Composable
fun SectionMap(
    modifier: Modifier = Modifier,
    mapUrl: String,
    coordinates: BusinessCoordinates,
    fullName: String
) {
    val context = LocalContext.current

    fun redirectToMaps() {
        val lat = coordinates.lat
        val lng = coordinates.lng

        val uri = "geo:0,0?q=${lat},${lng}(${fullName})".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)

        context.startActivity(intent)
    }

    Column(modifier) {
        Box(modifier = Modifier
            .padding(top = BasePadding)
            .fillMaxWidth()
            .height(220.dp)
            .clip(shape = ShapeDefaults.Large)
            .background(SurfaceBG)
            .clickable { redirectToMaps() }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(mapUrl)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .crossfade(true)
                    .build(),
                contentDescription = "Business Location Map",
                contentScale = ContentScale.Crop,
                onError = { Timber.tag("Business Map Error").e("ERROR: ${it.result.throwable.message}") },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(SpacingM))

        MainButton(
            title = stringResource(R.string.navigationDirections),
            onClick = { redirectToMaps() },
            colors = ButtonDefaults.buttonColors(
                containerColor = SurfaceBG,
                contentColor = OnSurfaceBG
            )
        )
    }
}