#!/bin/bash

# Replace the Android version in the gradle/libs.versions.toml file
# with the version that is compatible with Qodana
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS
    sed -i '' 's/com-android-application = "[^"]*"/com-android-application = "8.5.2"/' gradle/libs.versions.toml
else
    # Linux
    sed -i 's/com-android-application = "[^"]*"/com-android-application = "8.5.2"/' gradle/libs.versions.toml
fi