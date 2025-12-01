package com.example.scrollbooker.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.location.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class GeoPoint(
    val lat: Double,
    val lng: Double
)

enum class PrecisionMode {
    HIGH,
    BALANCED,
    LOW
}

enum class LocationPermissionStatus {
    NOT_DETERMINED,
    GRANTED,
    DENIED_CAN_ASK_AGAIN,
    DENIED_PERMANENTLY
}

data class LocationState(
    val lastAccurateLocation: GeoPoint? = null,
    val lastUpdateTimestamp: Long? = null,
    val permissionStatus: LocationPermissionStatus = LocationPermissionStatus.NOT_DETERMINED,
    val isUpdating: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = _state.asStateFlow()

    private var hasRequestedPermission: Boolean = false

    private var updatesJob: Job? = null
    private var currentMode: PrecisionMode? = null

    private fun resolveStatus(
        isGranted: Boolean,
        canAskAgain: Boolean,
    ): LocationPermissionStatus {
        return when {
            isGranted -> LocationPermissionStatus.GRANTED
            canAskAgain -> LocationPermissionStatus.DENIED_CAN_ASK_AGAIN
            else -> LocationPermissionStatus.DENIED_PERMANENTLY
        }
    }

    fun syncInitialPermission(isGranted: Boolean) {
        _state.update { it.copy(permissionStatus = if(isGranted) {
            LocationPermissionStatus.GRANTED
        } else {
            LocationPermissionStatus.NOT_DETERMINED
        }) }
    }

    fun onPermissionResult(granted: Boolean, canAskAgain: Boolean) {
        hasRequestedPermission = true

        val status = resolveStatus(
            isGranted = granted,
            canAskAgain = canAskAgain
        )

        _state.update { it.copy(permissionStatus = status) }

        if (!granted) {
            Timber.tag("User Location").e("User didn't provide location permission. Granted: $granted")
            stopLocationUpdates()
        }
    }

    fun startLocationUpdates(precisionMode: PrecisionMode) {
        val currentState = _state.value

        if(currentState.permissionStatus != LocationPermissionStatus.GRANTED) {
            Timber.tag("User Location").e("Start Location Updates. Permission not granted. Status=${currentState.permissionStatus}")
            return
        }

        if(updatesJob?.isActive == true && currentMode == precisionMode) {
            Timber.tag("User Location").e("Start Location Updates. Already running with $precisionMode")
        }

        if(updatesJob?.isActive == true && currentMode != precisionMode) {
            Timber.tag("User Location").e("Start Location Updates. Switching from $currentMode to $precisionMode")
            updatesJob?.cancel()
        }

        updatesJob = viewModelScope.launch {
            Timber.tag("User Location").e("Start Location Updates. Launching job, mode=$precisionMode")

            locationRepository.locationUpdates(precisionMode)
                .onStart {
                    Timber.tag("User Location").e("Start Location Updates. onStart Location updates")
                    _state.update { it.copy(isUpdating = true, error = null) }
                }
                .catch { e ->
                    Timber.tag("User Location").e("ERROR: on Fetching User Location: $e")
                    _state.update {
                        it.copy(
                            isUpdating = false,
                            error = e.message ?: "Location error"
                        )
                    }
                    updatesJob = null
                    currentMode = null
                }
                .collect { geoPoint ->
                    Timber.tag("User Location").e("Start Location Updates. Collect Point: $geoPoint")
                    _state.update {
                        it.copy(
                            lastAccurateLocation = geoPoint,
                            lastUpdateTimestamp = System.currentTimeMillis(),
                            isUpdating = true,
                            error = null
                        )
                    }
                }
        }
    }

    fun stopLocationUpdates() {
        updatesJob?.cancel()
        updatesJob = null
        currentMode = null

        _state.update { it.copy(isUpdating = false) }
    }

    fun fetchSingleLocation(precisionMode: PrecisionMode) {
        if (_state.value.permissionStatus != LocationPermissionStatus.GRANTED) return

        viewModelScope.launch {
            try {
                val geoPoint = locationRepository
                    .locationUpdates(precisionMode)
                    .first()

                _state.update {
                    it.copy(
                        lastAccurateLocation = geoPoint,
                        lastUpdateTimestamp = System.currentTimeMillis(),
                        isUpdating = false,
                        error = null
                    )
                }
            } catch (e: Throwable) {
                Timber.tag("User Location").e("ERROR: $e")
                _state.update {
                    it.copy(
                        isUpdating = false,
                        error = e.message ?: "Location Error"
                    )
                }
            }
        }
    }
}
