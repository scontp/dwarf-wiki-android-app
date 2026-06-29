#!/usr/bin/env bash
# Simplified Gradle Wrapper stub to trigger a real Gradle installation in CI
set -e
if [ "$1" == "assembleDebug" ]; then
  echo "Invoking Gradle..."
  gradle assembleDebug
else
  gradle "$@"
fi
