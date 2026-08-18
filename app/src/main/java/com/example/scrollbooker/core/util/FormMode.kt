package com.example.scrollbooker.core.util

sealed interface FormMode {
    object Edit : FormMode
    object Create : FormMode
    object Delete: FormMode
}