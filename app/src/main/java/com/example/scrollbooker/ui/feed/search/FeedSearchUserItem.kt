package com.example.scrollbooker.ui.feed.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.search.domain.model.SearchUser

@Composable
fun FeedSearchUserItem(user: SearchUser) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = BasePadding,
                vertical = SpacingM
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = Color.Gray
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = BasePadding)
            ) {
                Text(text = user.fullname)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if(user.isBusinessOrEmployee) user.profession else "@${user.username}",
                        color = Color.Gray
                    )
                    user.distance?.let {
                        Spacer(Modifier.width(SpacingS))
                        Box(modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                        )
                        Spacer(Modifier.width(SpacingS))
                        Text(
                            text = "${user.distance}km",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Avatar(
            url = user.avatar ?: "",
            size = 50.dp
        )
    }
}