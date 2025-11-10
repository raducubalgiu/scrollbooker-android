package com.example.scrollbooker.ui.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.customized.RatingsStars
import com.example.scrollbooker.core.util.Dimens.AvatarSizeXS
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun AppointmentDetailsWrittenReview(
    customerAvatar: String,
    review: String?,
    rating: Int,
    onOpenCancelSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpacingXL)
            .clip(shape = ShapeDefaults.Medium)
            .background(SurfaceBG)
            .padding(BasePadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(
                    url = customerAvatar,
                    size = AvatarSizeXS
                )

                Spacer(Modifier.width(SpacingS))

                Column {
                    Text(
                        text = "Ai evaluat $rating din 5",
                        color = Color.Gray
                    )
                    RatingsStars(
                        starSize = 18.dp,
                        rating = 4f
                    )
                }
            }

            IconButton(onClick = onOpenCancelSheet) {
                Icon(
                    painter = painterResource(R.drawable.ic_elipsis_vertical),
                    contentDescription = null
                )
            }
        }

        if(review?.isNotEmpty() == true) {
            Spacer(Modifier.height(SpacingM))

            Text(
                text = review,
                fontStyle = FontStyle.Italic,
                color = OnSurfaceBG,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}