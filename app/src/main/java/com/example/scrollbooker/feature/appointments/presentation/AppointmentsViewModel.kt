package com.example.scrollbooker.feature.appointments.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentsViewModel @Inject constructor(

): ViewModel() {
    init {
        Timber.tag("Init").e("-> Appointments - View Model Created")
    }
}