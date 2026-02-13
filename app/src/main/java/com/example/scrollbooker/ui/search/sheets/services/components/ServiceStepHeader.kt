package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ServiceStepHeader(
    onBack: () -> Unit,
    serviceDomainName: String?,
    serviceDomainUrl: String?
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIconButton(
            boxSize = 60.dp,
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = onBack
        )

        Text(
            text = stringResource(R.string.advancedFiltering),
            style = titleMedium,
            fontWeight = FontWeight.Bold
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .padding(horizontal = BasePadding)
            .size(130.dp)
            .clip(CircleShape)
            .background(SurfaceBG),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = serviceDomainUrl,
                contentDescription = "Selected image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(Modifier.height(BasePadding))

        Text(
            text = serviceDomainName ?: "",
            style = headlineSmall,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}