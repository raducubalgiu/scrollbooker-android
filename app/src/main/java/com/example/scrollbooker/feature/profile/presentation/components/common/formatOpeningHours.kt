package com.example.scrollbooker.feature.profile.presentation.components.common

import com.example.scrollbooker.feature.profile.domain.model.OpeningHours

fun formatOpeningHours(openingHours: OpeningHours): String {
    return if(openingHours.openNow) {
        "Inchide la ${openingHours.closingTime}"
    } else {
        "Deschide ${openingHours.nextOpenDay?.lowercase()} la ${openingHours.closingTime}"
    }
}