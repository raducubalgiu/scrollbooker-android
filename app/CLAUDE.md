# Claude Code Project Guide - Android Development

## Project Overview
- **Language**: Kotlin 2.x
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture
- **Asynchronous Flow**: Kotlin Coroutines & StateFlow/SharedFlow
- **Dependency Injection**: Hilt / Dagger

## Build & Run Commands
- **Build project**: `./gradlew assembleDebug`
- **Run unit tests**: `./gradlew test`
- **Run instrumented tests**: `./gradlew connectedAndroidTest`
- **Lint/Checkstyle**: `./gradlew lint`
- **Clean build**: `./gradlew clean`

## Code Style & Rules
- **Formatting**: Follow official Kotlin style guides. Keep lines under 120 characters.
- **UI Code**: Use Jetpack Compose exclusively. Avoid XML layouts.
- **State Management**: ViewModels must expose immutable state via `StateFlow` (e.g., `_uiState` private mutable, `uiState` public immutable).
- **Resources**: Hardcoded strings are strictly forbidden. Always use `stringResource(R.string.id)` and add them to `strings.xml`.
- **Previews**: Every `@Composable` screen/component must include a `@Preview` with a default theme wrapper.
- **Null Safety**: Leverage Kotlin's null safety features; avoid using `!!` (double bang) operators.

## Architecture Guidelines
- **Data Layer**: Repositories must handle data caching and network requests. Return `Resource<T>` or `Result<T>` wrappers to the Domain layer.
- **Domain Layer**: Use cases / Interactors should contain single business logic operations and be reusable.
- **UI Layer**: Composable functions must be stateless. Pass events/lambdas up to the ViewModel (State Hoisting).

## AI Assistance Instructions
- **Do not** write boilerplate code if standard Kotlin features or libraries handle it natively.
- Always implement proper error handling and loading states for UI operations.
- Prioritize memory efficiency (e.g., use `remember` and `rememberUpdatedState` properly in Compose).
- When writing files or refactoring, provide precise diffs or code blocks.
