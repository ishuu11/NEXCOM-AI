# NexAI

NexAI is a customizable Android assistant foundation. Version 1.0 provides a Compose app shell and saves assistant preferences locally. Voice recognition, command execution, background activation, and USB-C hardware are not implemented yet.

## Prerequisites

- Windows 10 or 11 (64-bit).
- Keep the project in an ASCII-only path on Windows, such as `C:\Android\NexAI`. Android Gradle Plugin may fail when the project path contains non-ASCII characters.
- Android Studio (current stable release).
- JDK 17 or newer. Android Studio's bundled JDK is suitable; the Gradle wrapper also uses `JAVA_HOME` or `java` on `PATH`.
- Android SDK Platform 36, Android SDK Build-Tools 35.0.0, and Android SDK Platform-Tools (ADB). Install these from Android Studio's SDK Manager if they are not already present.
- Internet access for the first Gradle sync/build so the wrapper and Maven dependencies can be downloaded.
- An Android 8.0 (API 26) or newer device, or an emulator with API 26 or newer.

The project uses Android Gradle Plugin 8.13.2, Kotlin 2.2.10, and Gradle 8.13. The checked-in Windows Gradle Wrapper downloads the pinned Gradle distribution and verifies its SHA-256 checksum.

## Open in Android Studio

1. Install Android Studio and complete its setup wizard.
2. In **SDK Manager**, install **Android 16 / API 36** (Android SDK Platform 36), **Android SDK Build-Tools 35.0.0**, and **Android SDK Platform-Tools**. Install an emulator image if needed.
3. Set Android Studio's **Gradle JDK** to JDK 17 or newer (the bundled JDK is suitable).
4. Choose **File > Open** and select the NexAI project root, the folder containing `settings.gradle.kts`.
5. Allow Gradle sync to finish. Accept Android SDK licenses if prompted.

## Build and run on Windows

Open PowerShell in the project root. The wrapper downloads Gradle 8.13 on its first run:

```powershell
.\gradlew.bat --version
.\gradlew.bat :app:assembleDebug
```

To run on an emulator, start one from **Device Manager**. To run on a physical device, enable Developer options and USB debugging, connect it by USB, and accept the device authorization prompt. Then install and launch the debug app:

```powershell
.\gradlew.bat :app:installDebug
```

The debug APK is written to `app\build\outputs\apk\debug\app-debug.apk`. The app's minimum supported Android version is API 26.

## Implemented in the current source

- Home, Activity, and Settings destinations with bottom navigation.
- Assistant name and activation phrase editing.
- English, Hindi, and Hinglish voice-language preference selection.
- Local persistence for the name, phrase, language, and background-activation preference using Preferences DataStore.
- A Home shortcut to Settings and an Activity empty state.
- UI copy that identifies voice controls and quick actions as coming soon.

These are source-level capabilities. Runtime behavior, navigation, and persistence across process restarts have **not been exercised in this environment** because Android Studio, the Android SDK, and an installed Gradle distribution are unavailable here.

## Planned

- Speech capture and recognition, including Hindi, English, and Hinglish.
- Wake phrase detection and supported background activation.
- Voice command routing and supported Android actions.
- Functional translation, timer, and other assistant actions.
- Optional USB-C hardware discovery and communication.

## Known limitations

- The microphone is not requested or used. Voice interaction and wake phrase activation do not work yet.
- The background-activation switch only saves a preference; it does not start a service or listen in the background.
- Quick action tiles other than **Make it yours** are visual placeholders and do not execute actions.
- Activity is an empty state; no conversation or action history is recorded.
- The USB-C section is informational; no device discovery or transport is implemented.
- There are no automated UI or persistence tests in the repository yet.

## Project structure

- `app/src/main/java/com/nexai/ui` — Compose screens and theme.
- `app/src/main/java/com/nexai/data` — Preferences DataStore repository and preference model.
- `app/src/main/AndroidManifest.xml` — launcher activity and app declaration.
- `gradle/wrapper` and `gradlew.bat` — pinned Gradle wrapper for Windows.
