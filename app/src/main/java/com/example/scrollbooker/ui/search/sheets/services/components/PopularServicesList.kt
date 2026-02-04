package com.example.scrollbooker.ui.search.sheets.services.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.search.sheets.services.steps.Servicee
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.headlineSmall
import com.example.scrollbooker.ui.theme.titleMedium

val fakeServices = listOf(
    Servicee(
        id = 1,
        name = "Tuns",
        url = "https://images.unsplash.com/photo-1599351431408-433ef72fe40b?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8aGFpcmN1dHxlbnwwfDB8MHx8fDI%3D"
    ),
    Servicee(
        id = 2,
        name = "Masaj",
        url = "https://images.unsplash.com/photo-1700522924565-9fad1c05469e?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTl8fG1hc3NhZ2V8ZW58MHx8MHx8fDI%3D"
    ),
    Servicee(
        id = 3,
        name = "ITP",
        url = ""
    ),
    Servicee(
        id = 4,
        name = "Polish",
        url = "https://images.unsplash.com/photo-1708805282695-ef186db20192?w=1200&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTR8fHBvbGlzaCUyMGF1dG98ZW58MHx8MHx8fDI%3D"
    ),
    Servicee(
        id = 5,
        name = "Psihoterapie",
        url = ""
    ),
    Servicee(
        id = 6,
        name = "Pensat",
        url = ""
    ),
    Servicee(
        id = 7,
        name = "Rinoplastie",
        url = ""
    )
)

@Composable
fun PopularServicesList() {
    Column {
        Spacer(Modifier.height(SpacingXL))

        Text(
            modifier = Modifier.padding(horizontal = BasePadding),
            style = headlineSmall,
            color = OnBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            text = "Servicii populare"
        )

        Spacer(Modifier.height(BasePadding))

        LazyVerticalGrid(
            contentPadding = PaddingValues(BasePadding),
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(BasePadding),
            horizontalArrangement = Arrangement.spacedBy(BasePadding),
        ) {
            items(fakeServices) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                    ) {
                        AsyncImage(
                            modifier = Modifier.matchParentSize(),
                            model = it.url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(SurfaceBG)
                        )
                    }

                    Spacer(Modifier.height(SpacingS))

                    Text(
                        text = it.name,
                        style = titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}