@echo off
REM Bijli Live - Mobile Testing Build Script
REM This script helps you build and test the app on your mobile phone

echo 🚀 Bijli Live - Mobile Testing Setup
echo ====================================

echo 📱 Preparing for mobile testing...
echo.

echo 📋 Step 1: Enable USB Debugging on Your Phone
echo    1. Go to Settings → About Phone
echo    2. Tap "Build Number" 7 times
echo    3. Go back to Settings → Developer Options
echo    4. Enable "USB Debugging"
echo    5. Enable "Install via USB" (if available)
echo.

echo 🔌 Step 2: Connect Your Phone
echo    1. Connect your Android phone to computer via USB cable
echo    2. Select "File Transfer" or "MTP" mode on phone
echo    3. Allow USB debugging when prompted
echo.

echo 🏗️ Step 3: Build APK Using Android Studio
echo    1. Open Android Studio
echo    2. File → Open → Select D:\bijli.live
echo    3. Wait for Gradle sync to complete
echo    4. Click "Run" button or press Shift+F10
echo    5. Select your connected phone from device list
echo    6. Click "OK" to install and run
echo.

echo 📱 Step 4: Test Your App
echo    ✅ App launches successfully
echo    ✅ All 7 modules load correctly
echo    ✅ Database connection established
echo    ✅ All features functional
echo.

echo 🎯 Alternative: Manual APK Build
echo    If Android Studio method doesn't work:
echo    1. In Android Studio: Build → Build Bundle(s) / APK(s) → Build APK(s)
echo    2. APK will be created at: app\build\outputs\apk\debug\app-debug.apk
echo    3. Transfer APK to phone and install manually
echo.

echo 🔧 Troubleshooting
echo    - Phone not detected: Check USB cable, enable USB debugging
echo    - App installation failed: Check storage space, enable unknown sources
echo    - App crashes: Check database connection, review logs
echo    - Database connection failed: Ensure PostgreSQL is running
echo.

echo 📊 Expected Results
echo    ✅ App installs successfully
echo    ✅ App launches without errors
echo    ✅ All 7 modules work correctly
echo    ✅ Database connection established
echo    ✅ All features functional
echo.

echo 🎉 Ready for Mobile Testing!
echo    Your bijli.live app is ready for mobile phone testing!
echo    Database with Karachi5846$ password will work on your phone!
echo.

pause
