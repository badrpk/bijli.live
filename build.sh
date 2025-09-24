#!/bin/bash

# Bijli Live Android App - Build Script
# This script helps set up and build the Android project

echo "🚀 Bijli Live Android App - Build Script"
echo "========================================"

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ ANDROID_HOME is not set. Please set it to your Android SDK path."
    echo "   Example: export ANDROID_HOME=/path/to/android-sdk"
    exit 1
fi

echo "✅ Android SDK found at: $ANDROID_HOME"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 8 or higher."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build the project
echo "🔨 Building the project..."
./gradlew build

# Check build result
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "📱 To run the app:"
    echo "   1. Connect an Android device or start an emulator"
    echo "   2. Run: ./gradlew installDebug"
    echo "   3. Or open the project in Android Studio and click Run"
    echo ""
    echo "🎉 Bijli Live app is ready!"
else
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi
