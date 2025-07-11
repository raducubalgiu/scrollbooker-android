package com.example.scrollbooker.components.customized

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PostGrid(
    post: Post,
    columnIndex: Int,
    onNavigateToPost: (String) -> Unit
) {
    val showRightBorder = columnIndex != 2

    Box(modifier = Modifier
        .aspectRatio(9f / 12f)
        //.border(0.5.dp, Divider)
        .background(SurfaceBG)
        .clickable(onClick = { onNavigateToPost("${MainRoute.ProfilePostDetail.route}/${post.id}") })
    ) {
        AsyncImage(
            model = post.mediaFiles.first().thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
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

        Box(modifier = Modifier
            .padding(
                top = 5.dp,
                start = 5.dp
            )
        ) {
            Box(modifier = Modifier
                .background(
                    color = Error,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(2.5.dp)
            ) {
                Text(
                    text = "Last Minute",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = bodyMedium
                )
            }
        }

        Row(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(
                bottom = 5.dp,
                start = 5.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play_outline),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "14,200",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                style = bodyLarge
            )
        }
    }
}