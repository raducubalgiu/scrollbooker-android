# ScrollBooker Android

ScrollBooker is a social-commerce & booking platform designed to connect users with businesses across beauty, wellness, auto services, and more.
The Android app provides a fast and modern mobile experience with video discovery, booking flows, and profile management.

---

## 🚀 Tech Stack

- [Kotlin](https://kotlinlang.org/) — Primary language
- [Jetpack Compose](https://developer.android.com/jetpack/compose) — Modern declarative UI
- [MVVM + Clean Architecture](https://developer.android.com/topic/architecture) — Scalable app structure
- [StateFlow / Coroutines](https://developer.android.com/kotlin/flow) — Reactive state management
- [Hilt / Custom DI](https://dagger.dev/hilt/) — Dependency injection
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) — Infinite scrolling lists
- [ExoPlayer](https://exoplayer.dev/) — Video playback (with caching & preload)
- [Retrofit + OkHttp](https://square.github.io/retrofit/) — Networking
- [Room / SQLDelight] — Local caching (if applicable)
- [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) — Date/time handling

---

## ✨ Features

- 🎥 **TikTok-style video feed** with preloading & caching (ExoPlayer)
- 📅 **Booking flows** with calendar & availability selection
- 🏢 **Business profiles** with services, media gallery, and reviews
- 👤 **User profiles** with posts, reposts, and saved content
- 🔑 **Secure authentication** with access/refresh tokens
- 📊 **Statistics & dashboards** for business accounts
- 📍 **Interactive maps** (Mapbox SDK) for business discovery

---

## 🛠️ Development Notes
- Integrated with the ScrollBooker backend (FastAPI + Postgres)
- Built for service-based industries: beauty, wellness, auto services, medical, etc.
- Modular & maintainable architecture for scalability
- Focused on reusability and developer productivity
