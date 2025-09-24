# Bijli Live - Android App Documentation

## 📱 Project Overview

Bijli Live is a comprehensive 5-level hierarchical Android application built with modern Android technologies. The app features 7 main modules with deep functionality across social interaction, marketplace, services, and personal management.

## 🏗️ Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Dependency Injection**: Hilt
- **Navigation**: Navigation Compose
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Camera**: CameraX
- **Video Player**: ExoPlayer
- **QR Code**: ZXing

### Project Structure
```
bijli.live/
├── app/
│   ├── build.gradle                    # App-level dependencies
│   ├── proguard-rules.pro             # ProGuard configuration
│   └── src/main/
│       ├── AndroidManifest.xml         # App manifest
│       ├── java/com/bijli/live/
│       │   ├── MainActivity.kt        # Main activity
│       │   ├── BijliApplication.kt    # Application class
│       │   └── ui/
│       │       ├── navigation/
│       │       │   └── BijliNavigation.kt
│       │       ├── components/
│       │       │   └── BottomNavigationBar.kt
│       │       ├── screens/
│       │       │   ├── camera/
│       │       │   │   └── CameraScreen.kt
│       │       │   ├── search/
│       │       │   │   └── SearchScreen.kt
│       │       │   ├── plus/
│       │       │   │   └── PlusScreen.kt
│       │       │   ├── chats/
│       │       │   │   └── ChatsScreen.kt
│       │       │   ├── discover/
│       │       │   │   └── DiscoverScreen.kt
│       │       │   ├── services/
│       │       │   │   └── ServicesScreen.kt
│       │       │   └── me/
│       │       │       └── MeScreen.kt
│       │       └── theme/
│       │           ├── Color.kt
│       │           ├── Theme.kt
│       │           └── Type.kt
│       └── res/
│           ├── values/
│           │   ├── strings.xml
│           │   └── themes.xml
│           ├── values-night/
│           │   ├── strings.xml
│           │   └── themes.xml
│           ├── drawable/
│           │   ├── ic_launcher_background.xml
│           │   └── ic_launcher_foreground.xml
│           ├── mipmap-anydpi-v26/
│           │   ├── ic_launcher.xml
│           │   └── ic_launcher_round.xml
│           └── xml/
│               ├── backup_rules.xml
│               └── data_extraction_rules.xml
├── build.gradle                       # Project-level configuration
├── settings.gradle                    # Project settings
├── gradle.properties                  # Gradle properties
├── build.sh                          # Build script
└── README.md                         # Project documentation
```

## 🎯 Features Implementation

### Level 1: Main Modules (7 Icons)

#### 1. 📷 Camera Module
**Features:**
- Photo capture with caption/text and emoji keyboard
- Skin tone variations (Default yellow, Light/Medium/Dark) with persistence
- Photo sharing with recipient selection from recent chats
- Preview before sending with 5-second undo option
- Video recording with trim length and auto-shorten
- 30-second preset with preview and save options
- Group call invitations via QR code with expiry settings (1 day/7 days)

**Implementation:**
- `CameraScreen.kt` with tabbed interface (Photo, Video, Group Call)
- Interactive dialogs for caption input, sharing, and QR generation
- State management for skin tone selection and media previews

#### 2. 🔍 Search Module
**Features:**
- Chat keyword search with highlighted results
- Scroll to first match with next/previous navigation
- Copy message text functionality
- Marketplace listings with category filtering
- Electronics subcategories (Mobiles, Laptops, Accessories, Appliances)

**Implementation:**
- `SearchScreen.kt` with tabbed interface (Chats, Marketplace)
- Real-time search with filtering and result highlighting
- Category-based filtering with subcategory support

#### 3. ＋ Plus Module
**Features:**
- Add contact with phone number validation
- Error messages with format suggestions and retry options
- New group creation with invite links and expiry settings
- Broadcast lists with media attachment and compression
- Linked devices management with active session tracking

**Implementation:**
- `PlusScreen.kt` with tabbed interface (Add Contact, New Group, Broadcast, Devices)
- Form validation with error handling and user guidance
- Device management with session tracking and controls

#### 4. 💬 Chats Module
**Features:**
- Recent chats with media sharing options
- Take new photos, upload from gallery, forward from other chats
- Saved rooms with invite sharing and link copying
- Support FAQs with topic browsing and payment information

**Implementation:**
- `ChatsScreen.kt` with tabbed interface (Recents, Saved Rooms, Support)
- Interactive chat lists with media sharing capabilities
- FAQ system with categorized support information

#### 5. 🌟 Discover Module
**Features:**
- Video player with play/pause and fullscreen functionality
- Landscape lock, auto brightness, and swipe-to-exit controls
- Moments with comment system and emoji reactions (👍❤️😂😢😡)
- News bookmarking with deletion confirmation and undo options
- Post actions with reporting system and reason selection

**Implementation:**
- `DiscoverScreen.kt` with tabbed interface (Videos, Moments, News, Actions)
- Video player controls with brightness adjustment
- Social interaction features with emoji reactions and reporting

#### 6. 🧩 Services Module
**Features:**
- Marketplace with multi-image upload and drag reorder
- Category filtering for Fashion (Men's/Women's/Kids' wear, Accessories)
- Food delivery with cart management and quantity selection
- Ride hailing with ride types (Economy, Sedan, Luxury)
- Driver earnings tracking with trip history and CSV export
- Real estate with location mapping and pin drop functionality

**Implementation:**
- `ServicesScreen.kt` with tabbed interface (Marketplace, Food Delivery, Ride Hailing, Real Estate)
- Complex service integrations with state management
- Location services and mapping functionality

#### 7. 👤 Me Module
**Features:**
- Account management with profile photo editing
- Crop, rotate, and filter options for profile photos
- Pay & Services with transaction history and CSV export
- Date filtering (Today, This week, This month, Custom range)
- Favorites with full-screen view and sharing options
- My posts with stats, likes count, and daily charts
- Cards & offers with coupon management and cart integration
- Sticker gallery with auto-play animation and preview
- Settings with privacy controls and contact blocking
- Support with PIN requests and auto-message templates

**Implementation:**
- `MeScreen.kt` with extensive tabbed interface
- Comprehensive profile and settings management
- Advanced analytics and reporting features

## 🔧 Technical Implementation

### State Management
- **Local State**: Compose `remember` and `mutableStateOf` for UI state
- **Screen State**: ViewModels for complex state management
- **Data Persistence**: Room database for local storage
- **Network State**: Repository pattern with Retrofit

### Navigation
- **Bottom Navigation**: 7-tab bottom navigation bar
- **Tab Navigation**: TabRow for sub-navigation within screens
- **Dialog Navigation**: AlertDialog for modal interactions
- **Deep Linking**: Navigation Compose for programmatic navigation

### UI/UX Design
- **Material Design 3**: Modern Material Design components
- **Dark/Light Theme**: Automatic theme switching support
- **Responsive Layout**: Adaptive UI for different screen sizes
- **Accessibility**: Screen reader support and accessibility features
- **Animations**: Smooth Compose animations and transitions

### Permissions
- `INTERNET` - Network operations
- `CAMERA` - Photo/video capture
- `RECORD_AUDIO` - Video recording
- `WRITE_EXTERNAL_STORAGE` - Media saving
- `READ_EXTERNAL_STORAGE` - Gallery access
- `ACCESS_FINE_LOCATION` - Location services
- `ACCESS_COARSE_LOCATION` - Approximate location

## 🚀 Build and Deployment

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34
- Java 8 or higher
- Kotlin 1.9.10

### Build Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the app on device/emulator

### Build Script
Use the provided `build.sh` script for automated building:
```bash
./build.sh
```

### Gradle Commands
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Install on device
./gradlew installDebug

# Run tests
./gradlew test
```

## 📊 Performance Considerations

### Memory Management
- Efficient Compose state management
- Image loading optimization with Coil
- Proper lifecycle management with ViewModels

### Network Optimization
- Retrofit with OkHttp for efficient networking
- Request/response caching
- Error handling and retry mechanisms

### UI Performance
- LazyColumn for efficient list rendering
- Compose recomposition optimization
- Proper state hoisting

## 🔒 Security Features

### Data Protection
- Secure storage with Room database
- Input validation and sanitization
- Permission-based access control

### Privacy Controls
- Contact blocking and management
- Profile visibility controls
- Data export capabilities

## 🧪 Testing Strategy

### Unit Testing
- ViewModel testing
- Repository testing
- Utility function testing

### UI Testing
- Compose UI testing
- Navigation testing
- User interaction testing

### Integration Testing
- End-to-end user flows
- API integration testing
- Database integration testing

## 🔄 Future Enhancements

### Planned Features
- Real-time messaging with WebSocket
- Push notifications
- Offline mode support
- Advanced camera features (filters, effects)
- Payment integration
- Social features (likes, comments, shares)
- Analytics and crash reporting

### Technical Improvements
- Performance optimization
- Code refactoring
- Additional testing coverage
- Documentation improvements

## 📞 Support and Maintenance

### Issue Reporting
- GitHub Issues for bug reports
- Feature requests through GitHub Discussions
- Documentation improvements via Pull Requests

### Code Maintenance
- Regular dependency updates
- Security patches
- Performance optimizations
- Feature additions based on user feedback

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📚 Additional Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Hilt Documentation](https://dagger.dev/hilt/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Retrofit Documentation](https://square.github.io/retrofit/)
