package com.example.scrollbooker.ui.shared.posts.sheets.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXS

@Composable
fun LocationSheetShimmer() {
    val brush = rememberShimmerBrush()

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = SpacingXL)
    ) {
        Spacer(modifier = Modifier
            .fillMaxWidth(fraction = 0.3f)
            .height(20.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(brush)
        )

        Spacer(Modifier.height(SpacingM))

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(brush)
        )

        Spacer(Modifier.height(SpacingS))

        Spacer(modifier = Modifier
            .fillMaxWidth(fraction = 0.2f)
            .height(20.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(brush)
        )

        Spacer(modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(vertical = SpacingXL)
            .clip(shape = ShapeDefaults.ExtraLarge)
            .background(brush)
        )
    }
}