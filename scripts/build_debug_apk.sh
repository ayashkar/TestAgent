#!/usr/bin/env bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

JAVA_17_DEFAULT="/root/.local/share/mise/installs/java/17.0.2"
if [[ -d "$JAVA_17_DEFAULT" ]]; then
  export JAVA_HOME="$JAVA_17_DEFAULT"
fi

if [[ ! -x "./gradlew" ]]; then
  echo "Gradle wrapper not found. Trying system Gradle..."
  gradle --no-daemon -Dorg.gradle.java.home="$JAVA_HOME" :app:assembleDebug
else
  ./gradlew --no-daemon -Dorg.gradle.java.home="$JAVA_HOME" :app:assembleDebug
fi

echo
echo "Debug APK path:"
echo "  app/build/outputs/apk/debug/app-debug.apk"
