package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.outlined.CarCrash
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

data class BusinessDomain(
    val id: Int,
    val name: String,
    val shortName: String,
    val serviceDomains: List<ServiceDomain>
)

fun BusinessDomain.getIcon(): ImageVector? {
    val icon = when(shortName) {
        "Beauty" -> Icons.Outlined.FavoriteBorder
        "Medical" -> Icons.Outlined.MedicalServices
        "Auto" -> Icons.Outlined.CarCrash
        "Fitness & Wellness" -> Icons.Outlined.FitnessCenter
        else -> null
    }

    return icon
}