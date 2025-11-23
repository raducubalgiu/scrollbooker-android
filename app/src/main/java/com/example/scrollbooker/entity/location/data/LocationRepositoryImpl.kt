package com.example.scrollbooker.entity.location.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.example.scrollbooker.ui.GeoPoint
import com.example.scrollbooker.ui.PrecisionMode
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationRepository {
    @SuppressLint("MissingPermission")
    override fun locationUpdates(precisionMode: PrecisionMode): Flow<GeoPoint> = callbackFlow {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        val (priority, intervalMs) = when (precisionMode) {
            PrecisionMode.HIGH -> Priority.PRIORITY_HIGH_ACCURACY to 15_000L
            PrecisionMode.BALANCED -> Priority.PRIORITY_BALANCED_POWER_ACCURACY to 30_000L
            PrecisionMode.LOW -> Priority.PRIORITY_LOW_POWER to 60_000L
        }

        val request = LocationRequest.Builder(priority, intervalMs)
            .setMinUpdateIntervalMillis(intervalMs)
            .setWaitForAccurateLocation(precisionMode == PrecisionMode.HIGH)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                trySend(
                    GeoPoint(
                        lat = location.latitude,
                        lng = location.longitude
                    )
                ).isSuccess
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedClient.removeLocationUpdates(callback)
        }
    }
}