package com.example.scrollbooker.ui.feed.drawer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.labelSmall

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedDrawerDomainSection(
    domain: ServiceDomain,
    selectedServiceIds: Set<Int>,
    onToggleService: (Int) -> Unit
) {
    val services = domain.services.orEmpty()
    if (services.isEmpty()) return

    val selectedCount = services.count { it.id in selectedServiceIds }

    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!domain.thumbnailUrl.isNullOrBlank()) {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(shape = ShapeDefaults.Small),
                    model = domain.thumbnailUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(SpacingM))
            }

            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = domain.name,
                style = bodyLarge,
                color = Color(0xFFE0E0E0),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            AnimatedVisibility(
                visible = selectedCount > 0,
                enter = scaleIn(tween(150)) + fadeIn(tween(150)),
                exit = scaleOut(tween(150)) + fadeOut(tween(150))
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = SpacingS)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$selectedCount",
                        style = labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimary
                    )
                }
            }
        }

        Spacer(Modifier.height(BasePadding))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            services.forEach { service ->
                FeedDrawerServiceChip(
                    label = service.shortName,
                    isSelected = service.id in selectedServiceIds,
                    onClick = { onToggleService(service.id) }
                )
            }
        }
    }
}
