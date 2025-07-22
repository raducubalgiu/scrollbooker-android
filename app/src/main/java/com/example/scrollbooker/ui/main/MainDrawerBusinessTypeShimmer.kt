package com.example.scrollbooker.ui.main
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.ShimmerMode
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush

@Composable
fun MainDrawerBusinessTypeShimmer() {
    val brush = rememberShimmerBrush(mode = ShimmerMode.DARK)

    Column(Modifier.fillMaxSize()) {
        repeat(2) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(55.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .width(50.dp)
                        .clip(shape = ShapeDefaults.ExtraLarge)
                        .background(brush)
                )
            }
        }
    }
}