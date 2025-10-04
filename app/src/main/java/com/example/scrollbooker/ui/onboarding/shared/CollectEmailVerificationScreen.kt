package com.example.scrollbooker.ui.onboarding.shared

import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.layout.FormLayout

@Composable
fun CollectEmailVerificationScreen(
    onNext: () -> Unit
) {
    FormLayout(
        headerTitle = "Email Verification",
        headLine = "",
        subHeadLine = "",
        buttonTitle = "Verify",
        onNext = onNext
    ) {}
}