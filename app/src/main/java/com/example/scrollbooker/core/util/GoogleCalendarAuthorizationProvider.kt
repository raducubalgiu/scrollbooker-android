package com.example.scrollbooker.core.util

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private val CALENDAR_SCOPE = Scope("https://www.googleapis.com/auth/calendar")

sealed interface GoogleCalendarAuthorization {
    data class Authorized(val serverAuthCode: String): GoogleCalendarAuthorization
    data class ResolutionRequired(val intentSender: IntentSender): GoogleCalendarAuthorization
}

/**
 * Requests offline access (a one-time serverAuthCode) to the user's Google Calendar via the
 * native Play Services Authorization API - no browser/WebView hop. `webClientId` must be a
 * Google Cloud "Web application" OAuth client whose id/secret the backend also holds, since the
 * backend is the one exchanging this code for the actual access/refresh tokens (redirect_uri
 * "postmessage") and persisting them in `calendar_connections`.
 */
class GoogleCalendarAuthorizationProvider @Inject constructor() {
    suspend fun authorize(activity: Activity, webClientId: String): Result<GoogleCalendarAuthorization> =
        runSuspendCatching {
            val request = AuthorizationRequest.builder()
                .setRequestedScopes(listOf(CALENDAR_SCOPE))
                .requestOfflineAccess(webClientId, /* forceCodeForRefreshToken = */ true)
                .build()

            try {
                val result = Identity.getAuthorizationClient(activity).authorize(request).await()
                val serverAuthCode = requireNotNull(result.serverAuthCode) {
                    "Google returned an authorization result without a serverAuthCode"
                }
                GoogleCalendarAuthorization.Authorized(serverAuthCode)
            } catch (e: ResolvableApiException) {
                GoogleCalendarAuthorization.ResolutionRequired(e.resolution.intentSender)
            }
        }

    /** Call after the consent Intent launched from [GoogleCalendarAuthorization.ResolutionRequired] returns. */
    fun extractServerAuthCode(activity: Activity, data: Intent?): Result<String> = runSuspendCatching {
        val result = Identity.getAuthorizationClient(activity).getAuthorizationResultFromIntent(data)
        requireNotNull(result.serverAuthCode) {
            "Google returned an authorization result without a serverAuthCode"
        }
    }
}
