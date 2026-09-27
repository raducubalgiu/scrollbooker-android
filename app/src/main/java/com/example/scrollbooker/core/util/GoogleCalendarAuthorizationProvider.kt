package com.example.scrollbooker.core.util

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// Read+write events (not calendar list/settings - narrower than the full "calendar" scope,
// which is "restricted" tier and needs a CASA assessment). ScrollBooker both imports existing
// Google Calendar events as blocked slots AND exports real ScrollBooker bookings as events on
// the user's own calendar (see service/integration/calendar_export.py on the backend) - matches
// scrollbooker-ios's GoogleCalendarAuthorizationProvider.swift, same Google Cloud project. Still
// "sensitive" tier, not "restricted".
private val CALENDAR_SCOPE = Scope("https://www.googleapis.com/auth/calendar.events")

sealed interface GoogleCalendarAuthorization {
    data class Authorized(val serverAuthCode: String): GoogleCalendarAuthorization
    data class ResolutionRequired(val intentSender: IntentSender): GoogleCalendarAuthorization
}

/**
 * Requests offline access (a one-time serverAuthCode) to the user's Google Calendar via the
 * native Play Services Authorization API - no browser/WebView hop. `webClientId` must be a
 * Google Cloud "Web application" OAuth client whose id/secret the backend also holds, since the
 * backend is the one exchanging this code for the actual access/refresh tokens (redirect_uri
 * "", per Google's own iOS/Android offline-access docs - not "postmessage", that's the JS/web
 * convention) and persisting them in `calendar_connections`.
 */
class GoogleCalendarAuthorizationProvider @Inject constructor(
    private val googleCredentialProvider: GoogleCredentialProvider
) {
    suspend fun authorize(activity: Activity, webClientId: String): Result<GoogleCalendarAuthorization> =
        runSuspendCatching {
            // AuthorizationClient.authorize() needs an established "default account" to have
            // anything to silently reauthorize against - without a prior Credential Manager
            // sign-in, it can return a "successful" result with no serverAuthCode at all,
            // regardless of scope (verified against developer.android.com/identity/authorization).
            // The id token itself isn't needed here, only the account-selection side effect.
            googleCredentialProvider.getIdToken(activity, webClientId).getOrThrow()

            val request = AuthorizationRequest.builder()
                .setRequestedScopes(listOf(CALENDAR_SCOPE))
                .requestOfflineAccess(webClientId, /* forceCodeForRefreshToken = */ true)
                .build()

            // authorize() always completes successfully for this API - it never throws to signal
            // "consent needed" the way older Google Sign-In APIs did via ResolvableApiException.
            // Whether a fresh consent screen is required is instead reported on the successful
            // result itself, via hasResolution()/pendingIntent (per Google's own sample at
            // developer.android.com/identity/authorization). Reading serverAuthCode unconditionally
            // without this check is what silently skipped the actual consent screen before.
            val result = Identity.getAuthorizationClient(activity).authorize(request).await()

            if (result.hasResolution()) {
                val pendingIntent = requireNotNull(result.pendingIntent) {
                    "Google authorization result reports hasResolution() but has no pendingIntent"
                }
                GoogleCalendarAuthorization.ResolutionRequired(pendingIntent.intentSender)
            } else {
                val serverAuthCode = requireNotNull(result.serverAuthCode) {
                    "Google returned an authorization result without a serverAuthCode"
                }
                GoogleCalendarAuthorization.Authorized(serverAuthCode)
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
