# Bijli Live - Testing Guide

## 🚀 Quick Start Testing

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34
- PostgreSQL database running
- Android device or emulator

## 📱 Mobile Testing (Android Device/Emulator)

### 1. Setup Android Device/Emulator
```bash
# Enable Developer Options on your Android device:
# Settings > About Phone > Tap "Build Number" 7 times
# Settings > Developer Options > Enable "USB Debugging"

# For Emulator:
# Open Android Studio > AVD Manager > Create Virtual Device
# Choose Pixel 6 or similar with API 34
```

### 2. Build and Install App
```bash
# Navigate to project directory
cd D:\bijli.live

# Clean and build
./gradlew clean
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Or use Android Studio:
# Click "Run" button or Shift+F10
```

### 3. App Paths on Device
- **Installed APK**: `/data/app/com.bijli.live-[random]/base.apk`
- **App Data**: `/data/data/com.bijli.live/`
- **Cache**: `/data/data/com.bijli.live/cache/`
- **Files**: `/data/data/com.bijli.live/files/`

### 4. Testing Features
1. **Camera Module**: Test photo capture, video recording, QR code generation
2. **Search Module**: Test chat search, marketplace filtering
3. **Plus Module**: Test contact addition, group creation
4. **Chats Module**: Test messaging, media sharing
5. **Discover Module**: Test video player, social interactions
6. **Services Module**: Test marketplace, food delivery, ride hailing
7. **Me Module**: Test profile management, transactions

## 💻 Computer Testing (Android Studio)

### 1. Project Setup
```bash
# Clone/Open project in Android Studio
# File > Open > Select D:\bijli.live

# Sync Gradle files
# Tools > Android > Sync Project with Gradle Files
```

### 2. Database Connection Setup
```bash
# Start PostgreSQL service
# Windows: Start PostgreSQL service in Services
# Linux/Mac: sudo systemctl start postgresql

# Create database
psql -U postgres -c "CREATE DATABASE bijli_live;"

# Run schema
psql -U postgres -d bijli_live -f database_schema.sql
```

### 3. Configuration Files
Update database connection in `DatabaseConfig.kt`:
```kotlin
data class DatabaseConfig(
    val host: String = "localhost", // or your server IP
    val port: Int = 5432,
    val database: String = "bijli_live",
    val username: String = "postgres",
    val password: String = "Karachi5846$"
)
```

### 4. Run Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Lint checks
./gradlew lint

# Build APK
./gradlew assembleDebug
```

## 🌐 Network Testing

### 1. Local Network Testing
```bash
# Find your computer's IP address
# Windows: ipconfig
# Linux/Mac: ifconfig

# Update database host in DatabaseConfig.kt to your IP
# Example: val host: String = "192.168.1.100"
```

### 2. Remote Database Testing
```bash
# For remote PostgreSQL server
# Update DatabaseConfig.kt with server details:
# val host: String = "your-server-ip"
# val port: Int = 5432
# val username: String = "postgres"
# val password: String = "Karachi5846$"
```

## 📊 Database Testing

### 1. Connect to Database
```bash
# Connect to PostgreSQL
psql -U postgres -d bijli_live

# Test connection
SELECT * FROM users LIMIT 5;
SELECT * FROM products LIMIT 5;
SELECT * FROM messages LIMIT 5;
```

### 2. Test Database Operations
```sql
-- Test user creation
INSERT INTO users (username, email, phone) VALUES 
('test_user', 'test@example.com', '+1234567890');

-- Test product creation
INSERT INTO products (seller_id, title, description, price, category) VALUES 
(1, 'Test Product', 'Test Description', 99.99, 'Electronics');

-- Test message creation
INSERT INTO messages (chat_id, sender_id, content) VALUES 
(1, 1, 'Test message');
```

## 🔧 Debugging

### 1. Android Studio Debugging
```bash
# Set breakpoints in code
# Run app in debug mode
# Use Logcat to view logs
# Use Debugger to inspect variables
```

### 2. Database Debugging
```bash
# Enable SQL logging in DatabaseModule.kt
# Check PostgreSQL logs
# Use pgAdmin for GUI database management
```

### 3. Network Debugging
```bash
# Use Android Studio Network Inspector
# Check Retrofit logs
# Use Charles Proxy or similar tools
```

## 📱 Device-Specific Testing

### 1. Different Screen Sizes
- Test on phones (5-7 inches)
- Test on tablets (8-12 inches)
- Test different resolutions

### 2. Different Android Versions
- Test on Android 7.0+ (API 24+)
- Test on latest Android 14 (API 34)

### 3. Performance Testing
```bash
# Monitor memory usage
# Check CPU usage
# Test battery consumption
# Test network usage
```

## 🧪 Automated Testing

### 1. Unit Tests
```bash
# Run unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "com.bijli.live.data.repository.BijliRepositoryTest"
```

### 2. UI Tests
```bash
# Run UI tests
./gradlew connectedAndroidTest

# Run on specific device
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.bijli.live.ui.CameraScreenTest
```

### 3. Integration Tests
```bash
# Test database connectivity
# Test API endpoints
# Test end-to-end user flows
```

## 📋 Testing Checklist

### ✅ Basic Functionality
- [ ] App launches successfully
- [ ] All 7 main modules load
- [ ] Navigation between screens works
- [ ] Database connection established
- [ ] User registration/login works

### ✅ Camera Module
- [ ] Photo capture works
- [ ] Video recording works
- [ ] QR code generation works
- [ ] Media sharing works
- [ ] Skin tone variations work

### ✅ Search Module
- [ ] Chat search works
- [ ] Marketplace filtering works
- [ ] Search results display correctly
- [ ] Category filtering works

### ✅ Plus Module
- [ ] Contact addition works
- [ ] Group creation works
- [ ] Device management works
- [ ] Validation works

### ✅ Chats Module
- [ ] Message sending works
- [ ] Media sharing works
- [ ] Group chats work
- [ ] Support system works

### ✅ Discover Module
- [ ] Video player works
- [ ] Social interactions work
- [ ] News bookmarking works
- [ ] Post reporting works

### ✅ Services Module
- [ ] Marketplace listing works
- [ ] Food ordering works
- [ ] Ride booking works
- [ ] Real estate mapping works

### ✅ Me Module
- [ ] Profile management works
- [ ] Transaction history works
- [ ] Settings work
- [ ] Support system works

## 🚨 Common Issues & Solutions

### 1. Database Connection Issues
```bash
# Check PostgreSQL service is running
# Verify credentials (Karachi5846$)
# Check firewall settings
# Verify database exists
```

### 2. Build Issues
```bash
# Clean project
./gradlew clean

# Invalidate caches in Android Studio
# File > Invalidate Caches and Restart

# Check Gradle version compatibility
```

### 3. Permission Issues
```bash
# Check AndroidManifest.xml permissions
# Grant permissions on device
# Test on different Android versions
```

## 📞 Support

For testing issues:
1. Check Android Studio Logcat
2. Check PostgreSQL logs
3. Verify network connectivity
4. Test on different devices
5. Check database schema

## 🎯 Performance Benchmarks

### Expected Performance
- App launch: < 3 seconds
- Screen transitions: < 500ms
- Database queries: < 100ms
- Image loading: < 2 seconds
- Video playback: Smooth 30fps

### Memory Usage
- Base app: < 100MB
- With images: < 200MB
- With videos: < 300MB

### Battery Usage
- Background: Minimal
- Active use: Moderate
- Video playback: High
