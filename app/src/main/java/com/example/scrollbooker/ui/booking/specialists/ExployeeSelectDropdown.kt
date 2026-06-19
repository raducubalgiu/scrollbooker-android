package com.example.scrollbooker.ui.booking.specialists

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlowUser

@Composable
fun EmployeeSelectDropdown(
    selectedEmployeeId: Int?,
    employees: List<BookingFlowUser>,
    onEmployeeSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentSelected = employees.find { it.id == selectedEmployeeId }

    val arrowRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .clickable { expanded = !expanded }
                .background(Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (expanded) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(100.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                if (currentSelected == null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Text(
                            text = stringResource(R.string.chooseSpecialist),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    SpecialistItem(
                        specialist = currentSelected,
                        withBadge = false,
                        size = 50.dp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(arrowRotation),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            employees.forEach { specialist ->
                DropdownMenuItem(
                    text = {
                        SpecialistItem(
                            specialist = specialist,
                            withBadge = true,
                            size = 60.dp
                        )
                    },
                    onClick = {
                        onEmployeeSelected(specialist.id)
                        expanded = false
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

@Composable
fun SpecialistItem(
    specialist: BookingFlowUser,
    withBadge: Boolean = true,
    size: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if(withBadge) {
            AvatarWithRating(
                url = specialist.avatar ?: "",
                size = size,
                rating = specialist.ratingsAverage,
                onClick = {}
            )
        } else {
            Avatar(
                url = specialist.avatar ?: "",
                size = size
            )
        }

        Column {
            Text(
                text = specialist.fullName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )
        }
    }
}
