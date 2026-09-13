package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.GoogleCalendarAuthorization
import com.example.scrollbooker.core.util.GoogleCalendarAuthorizationProvider
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.ConnectGoogleCalendarUseCase
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.DisconnectCalendarConnectionUseCase
import com.example.scrollbooker.entity.calendar.connection.domain.useCase.GetCalendarConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val GOOGLE_CALENDAR_WEB_CLIENT_ID =
    "596516500254-p64hb68ucv96j118frd8pa2ml9s3ksab.apps.googleusercontent.com"

@HiltViewModel
class CalendarConnectionViewModel @Inject constructor(
    private val getCalendarConnectionUseCase: GetCalendarConnectionUseCase,
    private val connectGoogleCalendarUseCase: ConnectGoogleCalendarUseCase,
    private val disconnectCalendarConnectionUseCase: DisconnectCalendarConnectionUseCase,
    private val googleCalendarAuthorizationProvider: GoogleCalendarAuthorizationProvider
): ViewModel() {
    private val _connectionState = MutableStateFlow<FeatureState<CalendarConnection?>>(FeatureState.Loading)
    val connectionState: StateFlow<FeatureState<CalendarConnection?>> = _connectionState.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _resolutionRequest = MutableStateFlow<IntentSender?>(null)
    val resolutionRequest: StateFlow<IntentSender?> = _resolutionRequest.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<SnackBarUiEvent.Show> = _events.asSharedFlow()

    init {
        viewModelScope.launch { loadConnection() }
    }

    private suspend fun loadConnection() {
        _connectionState.value = FeatureState.Loading

        getCalendarConnectionUseCase()
            .onSuccess { _connectionState.value = FeatureState.Success(it) }
            .onFailure { e ->
                Timber.tag("CalendarConnection").e(e, "ERROR: on fetching calendar connection")
                _connectionState.value = FeatureState.Error(e)
            }
    }

    fun connectGoogleCalendar(activity: Activity) {
        if (_isProcessing.value) return
        _isProcessing.value = true

        viewModelScope.launch {
            googleCalendarAuthorizationProvider.authorize(activity, GOOGLE_CALENDAR_WEB_CLIENT_ID)
                .onSuccess { outcome ->
                    when (outcome) {
                        is GoogleCalendarAuthorization.Authorized ->
                            finishConnecting(outcome.serverAuthCode)

                        // Consent UI required - the sheet launches it and reports back via
                        // onResolutionResult/onResolutionCancelled. isProcessing stays true meanwhile.
                        is GoogleCalendarAuthorization.ResolutionRequired ->
                            _resolutionRequest.value = outcome.intentSender
                    }
                }
                .onFailure { e ->
                    Timber.tag("CalendarConnection").e(e, "ERROR: on requesting Google Calendar authorization")
                    _isProcessing.value = false
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                }
        }
    }

    fun onResolutionHandled() {
        _resolutionRequest.value = null
    }

    fun onResolutionResult(activity: Activity, data: Intent?) {
        googleCalendarAuthorizationProvider.extractServerAuthCode(activity, data)
            .onSuccess { code -> viewModelScope.launch { finishConnecting(code) } }
            .onFailure { e ->
                Timber.tag("CalendarConnection").e(e, "ERROR: on resolving Google Calendar consent")
                _isProcessing.value = false
                _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
            }
    }

    fun onResolutionCancelled() {
        _isProcessing.value = false
    }

    private suspend fun finishConnecting(serverAuthCode: String) {
        connectGoogleCalendarUseCase(serverAuthCode)
            .onSuccess {
                _connectionState.value = FeatureState.Success(it)
                _isProcessing.value = false
            }
            .onFailure { e ->
                Timber.tag("CalendarConnection").e(e, "ERROR: on connecting Google Calendar")
                _isProcessing.value = false
                _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
            }
    }

    fun disconnect() {
        if (_isProcessing.value) return
        if ((_connectionState.value as? FeatureState.Success)?.data == null) return

        viewModelScope.launch {
            _isProcessing.value = true

            disconnectCalendarConnectionUseCase()
                .onSuccess {
                    _connectionState.value = FeatureState.Success(null)
                    _isProcessing.value = false
                }
                .onFailure { e ->
                    Timber.tag("CalendarConnection").e(e, "ERROR: on disconnecting Google Calendar")
                    _isProcessing.value = false
                    _events.tryEmit(SnackBarUiEvent.somethingWentWrong())
                }
        }
    }
}
