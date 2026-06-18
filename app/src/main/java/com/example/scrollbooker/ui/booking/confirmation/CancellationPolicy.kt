package com.example.scrollbooker.ui.booking.confirmation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CancellationPolicy(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceBG
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(BasePadding)) {
            Text(
                text = "Politica de anulare",
                style = titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(SpacingXXS))
            Text(
                text = "Anularea gratuită este disponibilă cu până la 24 de ore înainte de ora programată. Ulterior, se pot aplica taxe conform regulamentului afacerii.",
                color = Color.Gray
            )
        }
    }
}