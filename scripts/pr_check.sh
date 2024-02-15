#!/bin/bash

./gradlew check
./gradlew :ui:check
./gradlew :core:check
./gradlew :gross-plugin:check :gross-plugin:validatePlugins
