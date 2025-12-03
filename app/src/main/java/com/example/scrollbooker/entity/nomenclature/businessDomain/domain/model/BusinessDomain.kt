package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.ui.graphics.vector.ImageVector

data class BusinessDomain(
    val id: Int,
    val name: String,
    val shortName: String
)

fun BusinessDomain.getIcon(): ImageVector? {
    val icon = when(shortName) {
        "Beauty" -> Icons.Default.FavoriteBorder
        "Medical" -> Icons.Default.MedicalServices
        "Auto" -> Icons.Default.Build
        "Fitness & Wellness" -> Icons.Default.FitnessCenter
        else -> null
    }

    return icon
}