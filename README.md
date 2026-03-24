# PhotoDiary Android App

A simple Android app built with Kotlin + Jetpack Compose for saving diary entries with a photo label.

## Features

- Create diary entries with title, note, and photo label/URI.
- Displays entries in reverse chronological order.
- Material 3 UI.

## Run locally

1. Open the project in Android Studio.
2. Make sure the project uses **JDK 17** (AGP 8.x requirement).
3. Sync Gradle.
4. Run the `app` configuration on an emulator or Android device.

## Build a release APK/AAB

> You can build release artifacts locally, but publishing to Google Play requires your own Play Console account + signing key.

### 1) Create a signing key

```bash
keytool -genkey -v \
  -keystore photodiary-release.keystore \
  -alias photodiary \
  -keyalg RSA -keysize 2048 -validity 10000
```

### 2) Add signing values to `~/.gradle/gradle.properties`

```properties
PHOTODIARY_STORE_FILE=/absolute/path/to/photodiary-release.keystore
PHOTODIARY_STORE_PASSWORD=your_store_password
PHOTODIARY_KEY_ALIAS=photodiary
PHOTODIARY_KEY_PASSWORD=your_key_password
```

### 3) Configure signing in `app/build.gradle.kts`

Add a `signingConfigs { release { ... } }` and bind it in `buildTypes.release.signingConfig`.

### 4) Build a release bundle

```bash
./gradlew :app:bundleRelease
```

Output path:

- `app/build/outputs/bundle/release/app-release.aab`

### 5) Publish in Google Play Console

1. Create app in Play Console.
2. Complete **Store Listing**, **App Content**, **Data safety**, and **Privacy policy** sections.
3. Upload `app-release.aab` to an Internal testing track first.
4. Roll out to production when approved.

## Notes

- This project currently stores entries in memory only (not persistent storage yet).
- Next production steps: Room database, real image picker permissions flow, backup strategy, crash reporting, analytics, and privacy policy hosting.


## Quick test: build your APK

Run this from the project root:

```bash
./scripts/build_debug_apk.sh
```

If build succeeds, install this APK on your Android device/emulator:

- `app/build/outputs/apk/debug/app-debug.apk`

On a connected device, you can also install directly:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
