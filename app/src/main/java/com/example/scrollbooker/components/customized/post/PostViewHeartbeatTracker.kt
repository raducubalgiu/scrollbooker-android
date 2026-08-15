package com.example.scrollbooker.components.customized.post

import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.entity.social.post.data.remote.PostViewEventBulkItemRequest
import com.example.scrollbooker.entity.social.post.data.remote.PostsViewEventsBulkRequest
import com.example.scrollbooker.entity.social.post.domain.useCase.CreatePostsViewEventsBulkUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostViewHeartbeatTracker @Inject constructor(
    private val videoPlayerManager: VideoPlayerManager,
    private val createPostsViewEventsBulkUseCase: CreatePostsViewEventsBulkUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val trackerDispatcher = Dispatchers.Default.limitedParallelism(1)
    private val trackerScope = CoroutineScope(SupervisorJob() + trackerDispatcher)
    private val networkScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val sessions = HashMap<Int, ViewSession>()
    private val pendingEvents = ArrayDeque<PostViewEventBulkItemRequest>()
    private var flushInFlight = false

    private class ViewSession(
        val postId: Int,
        val source: PostViewSourceEnum,
        val durationMs: Int?,
        val sessionId: String = UUID.randomUUID().toString()
    ) {
        var isPlaying: Boolean = false
        var lastResumeElapsedRealtime: Long = 0L
        var tickerJob: Job? = null

        fun resume() {
            isPlaying = true
            lastResumeElapsedRealtime = SystemClock.elapsedRealtime()
        }

        fun consumeElapsed(): Int {
            if (!isPlaying) return 0
            val now = SystemClock.elapsedRealtime()
            val delta = (now - lastResumeElapsedRealtime).toInt()
            lastResumeElapsedRealtime = now
            return delta.coerceIn(0, MAX_DELTA_MS)
        }

        companion object {
            private const val MAX_DELTA_MS = 60_000
        }
    }

    init {
        trackerScope.launch {
            videoPlayerManager.playbackEvents.collect { event ->
                if (event.isPlaying) onPlaybackStarted(event) else onPlaybackStopped(event)
            }
        }

        trackerScope.launch {
            while (isActive) {
                delay(BATCH_FLUSH_INTERVAL_MS)
                flushBatchInternal()
            }
        }

        // Flush best-effort când aplicația trece în background — reduce fereastra de pierdere
        // de date față de a te baza doar pe intervalul programat de 15s.
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStop(owner: LifecycleOwner) {
                flushNow()
            }
        })
    }

    // ---- Sesiuni de playback ----
    private fun onPlaybackStarted(event: PlaybackEvent) {
        val isNew = !sessions.containsKey(event.postId)
        val session = sessions.getOrPut(event.postId) {
            ViewSession(
                postId = event.postId,
                source = sourceFor(event.scopeKey),
                durationMs = event.durationMs?.toInt()
            )
        }
        if (isNew) {
            Timber.tag(TAG).d(
                "SESSION START postId=%d source=%s session=%s",
                event.postId, session.source, session.sessionId
            )
        }

        session.tickerJob?.cancel()
        session.resume()

        session.tickerJob = trackerScope.launch {
            while (isActive) {
                delay(HEARTBEAT_INTERVAL_MS)
                val delta = session.consumeElapsed()
                if (delta > 0) {
                    val freshPosition = videoPlayerManager.currentPositionMs(event.scopeKey, event.postId)
                        ?: event.positionMs
                    enqueueEvent(buildEvent(session, positionMs = freshPosition.toInt(), deltaMs = delta))
                }
            }
        }
    }

    private fun onPlaybackStopped(event: PlaybackEvent) {
        val session = sessions.remove(event.postId) ?: return
        Timber.tag(TAG).d("SESSION END postId=%d session=%s", event.postId, session.sessionId)

        session.tickerJob?.cancel()
        session.tickerJob = null

        val delta = session.consumeElapsed()
        session.isPlaying = false

        if (delta > 0) {
            enqueueEvent(buildEvent(session, positionMs = event.positionMs.toInt(), deltaMs = delta))
        }
    }

    // ---- Buffer local + batching ----
    private fun buildEvent(session: ViewSession, positionMs: Int, deltaMs: Int): PostViewEventBulkItemRequest =
        PostViewEventBulkItemRequest(
            eventId = UUID.randomUUID().toString(),
            postId = session.postId,
            sessionId = session.sessionId,
            source = session.source,
            watchedMsDelta = deltaMs,
            positionMs = positionMs,
            mediaDurationMs = session.durationMs,
            capturedAt = System.currentTimeMillis(), // epoch millis, aliniat cu BE
            viewerFingerprintHash = null
        )

    private fun enqueueEvent(item: PostViewEventBulkItemRequest) {
        pendingEvents.addLast(item)

        if (pendingEvents.size > MAX_BUFFERED_EVENTS) {
            val dropped = pendingEvents.removeFirst()
            Timber.tag(TAG).w("Buffer overflow, dropping oldest event=%s", dropped.eventId)
        }

        if (pendingEvents.size >= FORCE_FLUSH_THRESHOLD) {
            trackerScope.launch { flushBatchInternal() }
        }
    }

    /** Flush best-effort, apelabil și din exterior dacă e nevoie (ex. logout explicit). */
    fun flushNow() {
        trackerScope.launch { flushBatchInternal() }
    }

    private fun flushBatchInternal() {
        if (flushInFlight || pendingEvents.isEmpty()) return

        val batch = pendingEvents.toList()
        flushInFlight = true

        networkScope.launch {
            val result = createPostsViewEventsBulkUseCase(
                PostsViewEventsBulkRequest(
                    clientBatchId = UUID.randomUUID().toString(),
                    events = batch
                )
            )

            withContext(trackerDispatcher) {
                if (result.isSuccess) {
                    pendingEvents.removeAll(batch.toSet())
                    val response = result.getOrNull()
                    Timber.tag(TAG).d(
                        "BATCH OK sent=%d accepted=%d rejected=%d",
                        batch.size, response?.accepted ?: -1, response?.rejected?.size ?: -1
                    )
                    if (!response?.rejected.isNullOrEmpty()) {
                        Timber.tag(TAG).w("BATCH partial rejects: %s", response.rejected)
                    }
                } else {
                    // Batch-ul rămâne în buffer — reîncercat automat la următorul flush programat.
                    Timber.tag(TAG).w("BATCH FAILED size=%d — %s", batch.size, result.exceptionOrNull()?.message)
                }
                flushInFlight = false
            }
        }
    }

    private fun sourceFor(scopeKey: String): PostViewSourceEnum {
        return when (scopeKey) {
            "explore_feed" -> PostViewSourceEnum.EXPLORE_FEED
            "following_feed" -> PostViewSourceEnum.FOLLOWING_FEED
            "my_profile_${PostViewSourceEnum.POST_DETAIL.key}" -> PostViewSourceEnum.POST_DETAIL
            "my_profile_${PostViewSourceEnum.BOOKMARK_POST_DETAIL.key}" -> PostViewSourceEnum.BOOKMARK_POST_DETAIL
            "user_profile_${PostViewSourceEnum.POST_DETAIL.key}" -> PostViewSourceEnum.POST_DETAIL
            else -> PostViewSourceEnum.OTHER
        }
    }

    companion object {
        private const val TAG = "PostViewHeartbeat"
        private const val HEARTBEAT_INTERVAL_MS = 5_000L
        private const val BATCH_FLUSH_INTERVAL_MS = 15_000L
        private const val MAX_BUFFERED_EVENTS = 200
        private const val FORCE_FLUSH_THRESHOLD = 30
    }
}