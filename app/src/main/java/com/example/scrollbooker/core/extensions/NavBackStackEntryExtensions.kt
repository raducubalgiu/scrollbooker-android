package com.example.scrollbooker.core.extensions

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy

fun NavBackStackEntry.isInRoute(route: String): Boolean {
    return destination.hierarchy.any { it.route == route }
}