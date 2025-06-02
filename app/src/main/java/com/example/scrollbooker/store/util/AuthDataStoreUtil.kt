package com.example.scrollbooker.store.util
import com.example.scrollbooker.store.AuthDataStore
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

suspend fun AuthDataStore.requireUserId(): Int {
    val userId = getUserId().firstOrNull()

    if (userId == null) {
        Timber.tag("DataStore").e("ERROR: on extracting User Id from DataStore")
        throw IllegalStateException("User not authenticated")
    }

    return userId
}