package com.example.scrollbooker.navigation.navigators

sealed interface NavigationEvent {
    object NavigateToProfile : NavigationEvent
}