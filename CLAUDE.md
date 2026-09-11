# CLAUDE.md — Scroll Booker (Android)

Guidance for Claude Code when working in this repository. This file is the
project-wide source of truth for architecture and conventions. A more
narrowly-scoped `app/CLAUDE.md` also exists with quick build commands and
general code-style rules — both apply; when they overlap, this file is the
more detailed/authoritative one for architecture and data-flow rules.

## Project Overview

- **Name**: Scroll Booker — a booking marketplace app with a TikTok/Instagram
  style vertical video feed (posts, reviews, appointments).
- **Language**: Kotlin, single Gradle module (`:app`).
- **UI**: Jetpack Compose exclusively (no XML layouts).
- **DI**: Hilt / Dagger.
- **Async**: Kotlin Coroutines + `StateFlow`/`SharedFlow`.
- **Networking**: Retrofit + Gson + OkHttp (logging interceptor in debug).
- **Media**: Media3 (ExoPlayer) for the video feed.
- **Paging**: Paging 3 for lists (appointments, feed, search, etc.).
- **Maps**: Mapbox.
- **Dates**: ThreeTenABP (`org.threeten.bp`), not `java.time` directly.
- **Testing**: JUnit4, MockK, Turbine, kotlinx-coroutines-test,
  androidx-paging-testing.

## Build & Verify

- Build debug: `./gradlew assembleDebug`
- Compile Kotlin only (fastest correctness check): `./gradlew :app:compileDebugKotlin`
- Full non-incremental recompile (use after touching shared/base components,
  or when in doubt about stale incremental state):
  `./gradlew :app:compileDebugKotlin --console=plain --rerun-tasks -Dkotlin.incremental=false`
- Unit tests: `./gradlew test`
- Instrumented tests: `./gradlew connectedAndroidTest`
- Lint: `./gradlew lint`

**Claude has no `adb`/emulator/device access in this environment.** A green
Gradle compile only proves the code compiles — it proves nothing about
runtime/visual behavior, gesture handling, animations, or layout. Always say
so explicitly when reporting on UI/behavioral changes, and ask the user to
verify on-device rather than declaring a fix "done."

## Module Layout

```
com.example.scrollbooker/
  entity/<domain>/<feature>/     # Clean Architecture, one package per feature
    data/
      remote/                    # Retrofit ApiService + request/response DTOs
      mappers/                   # Dto <-> Domain mapping (extension functions)
      repository/                # Repository implementation
    domain/
      model/                     # Domain models (no DTO/Gson annotations here)
      repository/                # Repository interface
      useCase/                   # One class per use case
    di/                          # Hilt module: ApiService, Repository, UseCases
  ui/<feature>/                  # Screens + ViewModels (presentation layer)
  components/
    core/                        # Generic design-system building blocks (Avatar, etc.)
    customized/                  # Feature-specific reusable composables (post feed, etc.)
  navigation/
    navigators/                  # Typed nav param classes + Navigator interfaces
    graphs/                      # Nested navigation() graph builders
    host/                        # NavHost per top-level tab/section
    transition/                  # Shared slide/fade transitions
  core/
    util/                        # FeatureState, runSuspendCatching, Dimens, withVisibleLoading...
    extensions/                  # Small extension functions (ResultExtensions, etc.)
    snackbar/                    # SnackBarController / UiText / CustomSnackBar
    enums/                       # Shared enums (status, channel, etc.)
    network/                     # OkHttp/Retrofit shared setup
  store/                         # DataStore-backed prefs (auth, theme)
  ui/theme/                      # Color.kt, Theme.kt, Type.kt
```

`entity/<domain>/<feature>` is the unit of feature isolation: a new
feature/endpoint gets its own `data/domain/di` triplet under `entity/`, and
its screens/ViewModels live under `ui/`. Follow the existing folder shape
exactly when adding a new feature — don't invent a different structure.

## The Core Rule: UseCase → `Result<T>`, ViewModel → `fold` / `FeatureState`

This is the single most important convention in the codebase. Some existing
use cases violate it (see "Known deviations" below) — **do not copy them**.
When you touch a use case that violates this rule, fix it as part of the
change instead of extending the anti-pattern.

### UseCases always return `Result<T>`, built with `runSuspendCatching`

```kotlin
class GetAppointmentByUserAndPostUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(userId: Int, postId: Int): Result<Appointment> {
        return runSuspendCatching {
            repository.getAppointmentByUserAndPost(userId, postId)
        }
    }
}
```

`runSuspendCatching` (`core/util/RunSuspendCatching.kt`) wraps the block in
`Result.success`/`Result.failure`, but **rethrows `CancellationException`**
instead of swallowing it — never replace it with a plain `try/catch` that
would break structured concurrency.

Rules for use cases:
- Return type is `Result<T>` (or `Flow<PagingData<T>>` for paging sources,
  which don't go through `Result`).
- The body is a thin orchestration over one or more repository calls, wrapped
  in `runSuspendCatching { ... }`. No manual `try/catch`, no `Timber` logging,
  no `FeatureState` inside a use case.
- **No UI-facing mapping or UI state inside a use case.** A use case returns
  a domain model (or a primitive/domain aggregate), never a `FeatureState`,
  never a UI/view-state object. Domain → UI mapping happens in the ViewModel
  (or in a `@Composable`/UI-layer mapper), never here.
- Use cases stay dependent only on domain-layer types (repository interfaces,
  domain models) — never on `core.util.FeatureState`, Compose, or Android UI
  types.

### `FeatureState<T>` is a UI-layer concept

```kotlin
// core/util/FeatureState.kt
sealed class FeatureState<out T> {
    object Loading: FeatureState<Nothing>()
    data class Success<T>(val data: T): FeatureState<T>()
    data class Error(val error: Throwable? = null): FeatureState<Nothing>()
}
```

`FeatureState` exists to drive Compose screens off a single `StateFlow`:
Loading/Success/Error map directly to what the screen renders (skeleton,
content, error view). It belongs **only** in the ViewModel/UI layer.

Convert a use case's `Result<T>` into `FeatureState<T>` with the existing
extension (`core/extensions/ResultExtensions.kt`):

```kotlin
fun <T> Result<T>.toFeatureState(): FeatureState<T> =
    fold(
        onSuccess = { FeatureState.Success(it) },
        onFailure = { FeatureState.Error(it as? Exception ?: Exception(it)) }
    )
```

### ViewModel pattern: `result.fold(...)`

The standard shape for a ViewModel loading data via a use case:

```kotlin
val schedulesState: StateFlow<FeatureState<List<Schedule>>> = userIdFlow
    .filterNotNull()
    .distinctUntilChanged()
    .flatMapLatest { userId ->
        flow {
            emit(FeatureState.Loading)

            val result = withVisibleLoading { getSchedulesByUserIdUseCase(userId) }

            emit(
                result.fold(
                    onSuccess = { FeatureState.Success(it) },
                    onFailure = { e ->
                        Timber.tag("Schedules").e("ERROR: on Fetching Schedules By User Id $e")
                        FeatureState.Error(e)
                    }
                )
            )
        }
    }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FeatureState.Loading)
```

For one-shot actions (save/create/delete) rather than a `StateFlow` of
content, still call the use case once and `result.fold(...)` it: on success
update local state / navigate / notify; on failure show a `SnackBarUiEvent`
via `SnackBarController` (see `core/snackbar/`) and log with `Timber`.

- Expose state as `StateFlow`/`SharedFlow` only: private mutable
  (`_thing: MutableStateFlow<...>`), public immutable (`thing: StateFlow<...>`).
- Use `withVisibleLoading { ... }` (`core/util/withVisibleLoading.kt`) around
  a use case call when you want a minimum perceived-loading duration (avoids
  skeleton flicker on fast responses).
- Tag `Timber` logs with the feature name, matching the existing style:
  `Timber.tag("Feature Name").e(e, "ERROR: on <action>")`.

### Known deviations (do not copy — fix when touched)

A handful of existing use cases return `FeatureState<T>` directly instead of
`Result<T>` (e.g. `GetBusinessProfileUseCase`). This is legacy/incorrect per
the rule above. If you're modifying one of these, migrate it to
`Result<T>` + `runSuspendCatching` and move the `FeatureState` mapping into
its ViewModel, rather than propagating the old pattern to new code.

## Data Layer Conventions

- One Retrofit `ApiService` interface per feature under `data/remote/`.
- DTOs are plain data classes under `data/remote/`; **always** add
  `@SerializedName("snake_case_key")` explicitly whenever the JSON key
  differs from the Kotlin property name. Gson does **not** throw when a name
  doesn't match — it silently leaves the field at its default (`0`, `false`,
  `null`), which is a very easy, very silent bug. Never rely on Gson's
  best-effort name matching for anything but an exact 1:1 match.
- Mapping between DTO and domain model lives in `data/mappers/` as extension
  functions: `fun XxxDto.toDomain(): Xxx`, `fun Xxx.toDto(): XxxDto`. Never
  put this logic inline in the repository or the use case.
- The repository **interface** lives in `domain/repository/` and speaks only
  domain models. The **implementation** lives in `data/repository/`, calls
  the `ApiService`, and maps DTO → domain immediately before returning.
- Paginated lists use Paging 3: a `PagingSource` under `data/remote/`, built
  via `Pager(PagingConfig(...)) { MyPagingSource(...) }.flow` in the
  repository impl, exposed as `Flow<PagingData<T>>` (not wrapped in `Result`).

## Dependency Injection (Hilt)

Each feature has one `@Module @InstallIn(SingletonComponent::class) object`
under `di/` that provides, in order: the Retrofit `ApiService` (built from
`BuildConfig.BASE_URL` + shared `OkHttpClient`), the repository
implementation (bound to the interface), and then every use case in that
feature explicitly via `@Provides`. Follow this same explicit
provide-every-use-case style for new features rather than relying solely on
`@Inject constructor` resolution, to match the existing modules.

## Navigation

- Navigation Compose, one `NavHost` per top-level tab/section under
  `navigation/host/`, nested graphs under `navigation/graphs/` using the
  `navigation(...)` builder.
- Typed navigation parameters are plain classes in
  `navigation/navigators/NavigationParams.kt` (e.g. `UserProfileParam`,
  `ReviewsParam`, `BookingParam`), passed via `NavHostController` extension
  functions rather than raw route strings with manual argument encoding.
- Shared slide/fade transitions live in `navigation/transition/`; reuse them
  for new routes instead of redefining animation specs per-screen.
- **Bottom sheet → full-screen navigation pattern**: when an action needs to
  navigate away while a bottom sheet is open (Edit, Statistics, Book
  Appointment, etc.), don't navigate immediately. Set a pending-action state,
  hide the sheet, and navigate from a `LaunchedEffect(sheetState.isVisible)`
  once the sheet has fully collapsed — see `PostSheetsHost`/`handlePostSheetAction`
  for the reference implementation. Navigating while the sheet is still
  animating away produces visual glitches.

## Compose UI Conventions

- Composables are stateless where practical; state is hoisted, events are
  passed up as lambdas to the ViewModel (unidirectional data flow).
- **Never hardcode spacing/sizes** — use `Dimens` (`core/util/Dimens.kt`):
  `SpacingXXS`…`SpacingXXL`, `AvatarSize*`, `IconSize*`. Add a new constant
  there rather than inlining a raw `.dp` value in a screen.
- **Never hardcode colors** — use the top-level theme accessors in
  `ui/theme/Color.kt` (`Primary`, `OnPrimary`, `Background`, `Error`,
  `Rating`, etc.), which wrap `MaterialTheme.colorScheme` / `ExtendedTheme`.
  Add a new token there (and to the light/dark `ExtendedColors`) rather than
  using a literal `Color(...)` or a raw MaterialTheme call inline.
- **Never hardcode user-facing strings** — every string goes in `strings.xml`
  and is read via `stringResource(R.string.id)`.
- Add a `@Preview` to screen-level composables with the app's theme wrapper.
- `components/core/` = generic, feature-agnostic design-system pieces
  (Avatar, buttons, etc.) — no feature/domain imports allowed here.
  `components/customized/` = feature-specific composites that *do* depend on
  domain models (e.g. the post feed, booking sheets).

### Overlapping touch targets in the video feed (`PostOverlay`/`VideoScrubber`)

The vertical video feed intentionally stacks several tappable regions on top
of each other (full-screen play/pause toggle, `Book Now`, `PostActions`,
`VideoScrubber`'s drag strip). This area is easy to regress — a few hard-won
rules from prior debugging sessions:

- Never use a plain `Spacer` inside `PostOverlay`'s content column for
  layout gaps — an empty `Spacer` still sits over the full-screen play/pause
  `clickable` and swallows taps meant to toggle playback. Use
  `TapAbsorbingSpacer` (`components/customized/post/components/TapAbsorbingSpacer.kt`)
  instead, which is Spacer-shaped but explicitly consumes the tap so it
  behaves like a real (no-op) UI element rather than a hole other elements'
  gestures fall through.
- `Modifier.zIndex()` only affects **paint order**. It does not affect
  pointer-input hit-test/dispatch order, and it does not propagate through a
  wrapper composable (e.g. `AnimatedVisibility`) to compete with a sibling
  declared outside that wrapper. Do not use `zIndex` to try to make one
  overlapping composable "win" touch priority over another — it doesn't do
  that.
- The declaration order of overlapping siblings inside a `Box` affects both
  paint order *and* the ambiguous default hit-test dispatch order between
  them — reordering to "fix" touch priority is fragile and has caused real
  regressions (broken drag, wrong opacity) in this codebase. Avoid relying on
  it.
- The **reliable** way to make one overlapping pointer-input consumer always
  yield to another for a plain tap while still owning drags: read events on
  `PointerEventPass.Initial` in the "owning" component (e.g. `VideoScrubber`),
  instead of the default `Main` pass. `Initial` is guaranteed by Compose to
  resolve, for every node in the tree, before `Main` resolves for any node —
  this is a hard ordering guarantee, unlike sibling z-order. Consume only
  once you've determined the gesture is actually a drag (touch-slop
  exceeded); leave a plain tap-and-release completely unconsumed so a
  `clickable` underneath (which reads `Main`) fires normally. See
  `VideoScrubber.kt`'s `pointerInput` block for the reference implementation.

## Coroutines / Flow Conventions

- ViewModel state: `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initialValue)`.
- Per-id keyed fetches (e.g. re-fetching when a post/user id changes) use
  `flatMapLatest` on a `MutableStateFlow` id source, with `filterNotNull()`/
  `distinctUntilChanged()` upstream to avoid redundant emissions.
- `runSuspendCatching` always rethrows `CancellationException` — don't add a
  broader `catch (e: Exception)` around a coroutine that would defeat
  structured-concurrency cancellation.

## Media3 / Video Player

Code against `androidx.media3.common.Player` (the interface), not the
concrete `ExoPlayer` class — this matches Media3's own guidance and avoids
unnecessary casts when a component only needs playback control, not
ExoPlayer-specific APIs. Only depend on `ExoPlayer` where you specifically
need an ExoPlayer-only API (e.g. building/configuring the player instance).

## Dates

Use `org.threeten.bp` (ThreeTenABP) types (`LocalDate`, `ZonedDateTime`,
etc.), consistent with the rest of the codebase — not `java.time` directly
(the project targets `minSdk = 24`, below `java.time`'s natural API level
without desugaring wired up for it here).

## Testing

- Unit tests live under `app/src/test/`, using JUnit4 + MockK + Turbine +
  `kotlinx-coroutines-test`.
- Test a ViewModel against a **fake repository** implementing the domain
  repository interface (not a mocked use case), constructing the real use
  case around the fake, and `mockk(relaxed = true)` only for collaborator
  use cases unrelated to the behavior under test — see
  `app/src/test/java/com/example/scrollbooker/ui/appointments/AppointmentsViewModelTest.kt`.
- Paging flows are asserted via `androidx.paging.testing.asSnapshot`.
- New use cases/ViewModels with non-trivial branching (success/error mapping,
  `flatMapLatest` re-fetch logic) should get a unit test following this
  pattern rather than being left untested.

## General Engineering Discipline

- Don't add abstractions, error handling, or config the current task doesn't
  need — three similar use cases are fine; a shared base class for them
  isn't, unless a fourth real caller shows up.
- Keep use cases single-purpose and thin; put actual business rules in the
  domain model (extension functions on the domain model, e.g.
  `Appointment.getDurationText()`) rather than in the use case or the
  ViewModel.
- When adding a field to a DTO/domain model that's optional on the backend,
  make it nullable in the DTO and map it with `.orEmpty()`/a sensible default
  in the mapper — don't push null-handling into every call site.
