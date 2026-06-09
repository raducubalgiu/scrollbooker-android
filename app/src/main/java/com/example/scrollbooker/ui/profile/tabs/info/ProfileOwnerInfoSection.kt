package com.example.scrollbooker.ui.profile.tabs.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAboutOwner
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ProfileInfoOwnerSection(
    owner: UserProfileAboutOwner,
    onNavigateToUserProfile: (userId: Int, username: String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, Divider),
        shape = ShapeDefaults.Large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpacingM)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "ANGAJAT LA",
                style = labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = OnBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(ShapeDefaults.Medium)
                        .background(SurfaceBG),
                    contentAlignment = Alignment.Center
                ) {
                    if (!owner.avatar.isNullOrBlank()) {
                        AsyncImage(
                            model = owner.avatar,
                            contentDescription = owner.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Business Placeholder",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = owner.fullName,
                        style = titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = owner.profession,
                        style = bodyMedium,
                        color = OnBackground.copy(alpha = 0.7f)
                    )
                }
            }

            OutlinedButton(
                onClick = { onNavigateToUserProfile(owner.id, owner.username) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = ShapeDefaults.Medium,
                border = BorderStroke(1.dp, Divider),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = OnBackground
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Vezi profilul business-ului",
                        fontWeight = FontWeight.Bold,
                        style = bodyLarge
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                        contentDescription = "Chevron Right",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}