package com.example.scrollbooker.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private var lastLocation: BusinessCoordinates? = null
    private var lastFetchElapsedRealtimeMs: Long? = null

    private val cacheTtlMs = 600_000L
    private val fetchTimeoutMs = 3_000L

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    suspend fun currentLocation(): BusinessCoordinates? {
        if (!hasLocationPermission()) {
            Timber.tag("UserLocationService").w("Location permission not granted, returning null")
            lastLocation = null
            lastFetchElapsedRealtimeMs = null
            return null
        }

        val cached = lastLocation
        val fetchedAt = lastFetchElapsedRealtimeMs
        if (cached != null && fetchedAt != null &&
            (android.os.SystemClock.elapsedRealtime() - fetchedAt) < cacheTtlMs
        ) {
            return cached
        }

        return fetchLocation()
    }

    private suspend fun fetchLocation(): BusinessCoordinates? = withContext(Dispatchers.Main) {
        val coordinates = fetchCachedLocation() ?: fetchFreshLocation()

        if (coordinates != null) {
            lastLocation = coordinates
            lastFetchElapsedRealtimeMs = android.os.SystemClock.elapsedRealtime()
        } else {
            Timber.tag("UserLocationService").w("Location fetch timed out or returned no fix after ${fetchTimeoutMs}ms")
        }

        coordinates
    }

    // The OS's own last-known fix (instant, no active GPS/network read triggered) - same call
    // SearchScreen.kt already relies on. Covers the common case (this device already resolved a
    // location for some app, ever) without ever touching the slower active-fix path below.
    private suspend fun fetchCachedLocation(): BusinessCoordinates? = try {
        fusedLocationClient.lastLocation.await()?.let {
            BusinessCoordinates(lat = it.latitude.toFloat(), lng = it.longitude.toFloat())
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: SecurityException) {
        null
    }

    // Only reached when the OS has no cached fix at all yet - actively requests one, bounded by
    // fetchTimeoutMs so a device/emulator with no location signal available doesn't stall callers.
    private suspend fun fetchFreshLocation(): BusinessCoordinates? = withTimeoutOrNull(fetchTimeoutMs) {
        try {
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
                .build()

            val location = fusedLocationClient.getCurrentLocation(request, null).await()
            location?.let {
                BusinessCoordinates(lat = it.latitude.toFloat(), lng = it.longitude.toFloat())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: SecurityException) {
            Timber.tag("UserLocationService").w(e, "Location permission was revoked mid-fetch")
            null
        }
    }
}
