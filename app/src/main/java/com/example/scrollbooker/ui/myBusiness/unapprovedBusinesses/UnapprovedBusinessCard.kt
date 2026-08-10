package com.example.scrollbooker.ui.myBusiness.unapprovedBusinesses
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelSmall

@Composable
fun UnapprovedBusinessCard(
    businessItem: UnapprovedBusiness,
    onApprove: (Int) -> Unit,
    onReject: (Int) -> Unit,
    isSaving: FeatureState<Unit>?
) {
    val isLoading = isSaving is FeatureState.Loading

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceBG)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(url = businessItem.avatar ?: "")

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = businessItem.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "@${businessItem.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = BasePadding),
                color = Divider
            )

            Text(
                text = "Tip business: ${businessItem.business.businessType.name}",
                style = bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Adresă: ${businessItem.business.location.address}",
                style = bodyMedium,
                color = OnSurfaceBG
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Primary.copy(alpha = 0.2f),
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = if (businessItem.business.hasEmployees) "Are angajați" else "Fără angajați",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = labelSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MainButtonOutlined(
                    title = "Reject",
                    onClick = { onReject(businessItem.id) },
                    modifier = Modifier.weight(1f),
                )

                MainButton(
                    title = "Approve",
                    onClick = { onApprove(businessItem.id) },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = SpacingXL,
                        vertical = SpacingM
                    ),
                    isLoading = isLoading,
                    enabled = !isLoading
                )
            }
        }
    }
}
