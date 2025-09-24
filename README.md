# Bijli Live - Android App

A comprehensive 5-level hierarchical Android application built with Jetpack Compose, featuring multiple modules for social interaction, marketplace, services, and more.

## 🏗️ Project Structure

The app follows a 5-level hierarchical feature tree:

### Level 1: Main Modules (7 icons)
- 📷 **Camera** - Photo/video capture and sharing
- 🔍 **Search** - Chat and marketplace search functionality  
- ＋ **Plus** - Add contacts, groups, and device management
- 💬 **Chats** - Messaging, saved rooms, and support
- 🌟 **Discover** - Videos, moments, news, and post actions
- 🧩 **Services** - Marketplace, food delivery, ride hailing, real estate
- 👤 **Me** - Profile, payments, favorites, settings

### Level 2: Sub-menus
Each main module contains multiple sub-menus for organized functionality.

### Level 3: Sub-items
Detailed features within each sub-menu.

### Level 4: Atomic Actions
Specific user actions and interactions.

### Level 5: Micro-options/Settings
Fine-grained controls and preferences.

## 🚀 Features

### 📷 Camera Module
- **Photo Capture**: Take photos with caption/text and emoji keyboard
- **Skin Tone Variations**: Default yellow, light/medium/dark options with persistence
- **Photo Sharing**: Choose recipients from recent chats with preview and undo
- **Video Recording**: Trim length, auto-shorten, 30s preset with preview
- **Group Calls**: Invite via QR code with expiry settings (1 day/7 days)

### 🔍 Search Module
- **Chat Search**: Keyword search with highlight results, scroll to matches
- **Marketplace Search**: Category filtering for electronics (mobiles, laptops, accessories, appliances)

### ＋ Plus Module
- **Add Contact**: Phone validation with error messages and suggestions
- **New Group**: Invite via link with expiry options (24h, 7-day, no expiry)
- **Broadcast Lists**: Send messages with media attachment and compression
- **Linked Devices**: Active sessions management with device details

### 💬 Chats Module
- **Recents**: Open chats, send media (photo, gallery, forward)
- **Saved Rooms**: Share invites with link copying and confirmation
- **Support**: FAQs with topic browsing and payment information

### 🌟 Discover Module
- **Videos**: Play/pause, fullscreen with landscape lock and auto brightness
- **Moments**: Comment with emoji reactions (👍❤️😂😢😡)
- **News**: Bookmark management with deletion confirmation and undo
- **Post Actions**: Report with reason selection (spam, inappropriate, harassment)

### 🧩 Services Module
- **Marketplace**: Upload multiple images with drag reorder and category filters
- **Food Delivery**: Browse restaurants, add to cart with quantity selection and notes
- **Ride Hailing**: Request rides (economy, sedan, luxury) with driver earnings tracking
- **Real Estate**: Location mapping with pin drop and auto-fill address

### 👤 Me Module
- **Account**: Edit profile with photo upload, crop, rotate, and filters
- **Pay & Services**: Transaction history with CSV export and date filtering
- **Favourites**: Full screen view with sharing options (WhatsApp, Facebook)
- **My Posts**: Stats with likes count, daily charts, and export functionality
- **Cards & Offers**: Coupon codes with cart application and discount display
- **Sticker Gallery**: Browse packs with auto-play animation and preview
- **Settings**: Privacy controls with contact blocking and management
- **Support**: PIN requests with auto-message templates

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Hilt dependency injection
- **Navigation**: Navigation Compose
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Camera**: CameraX
- **Video Player**: ExoPlayer
- **QR Code**: ZXing
- **Permissions**: Accompanist Permissions

## 📱 Requirements

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

## 🔧 Setup Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the app on device/emulator

## 📋 Permissions

The app requires the following permissions:
- `INTERNET` - For network operations
- `CAMERA` - For photo/video capture
- `RECORD_AUDIO` - For video recording
- `WRITE_EXTERNAL_STORAGE` - For saving media
- `READ_EXTERNAL_STORAGE` - For accessing gallery
- `ACCESS_FINE_LOCATION` - For location-based services
- `ACCESS_COARSE_LOCATION` - For approximate location

## 🎨 UI/UX Features

- **Material Design 3**: Modern Material Design components
- **Dark/Light Theme**: Automatic theme switching
- **Responsive Layout**: Adaptive UI for different screen sizes
- **Smooth Animations**: Compose animations and transitions
- **Accessibility**: Screen reader support and accessibility features

## 📊 Architecture

The app follows clean architecture principles:

```
├── UI Layer (Compose Screens)
├── ViewModel Layer (State Management)
├── Repository Layer (Data Access)
├── Data Layer (Local & Remote)
└── Domain Layer (Business Logic)
```

## 🔄 State Management

- **Compose State**: Local state management with `remember` and `mutableStateOf`
- **ViewModel**: Screen-level state management
- **Repository Pattern**: Centralized data access
- **Room Database**: Local data persistence

## 🚀 Future Enhancements

- Real-time messaging with WebSocket
- Push notifications
- Offline mode support
- Advanced camera features (filters, effects)
- Payment integration
- Social features (likes, comments, shares)
- Analytics and crash reporting

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📞 Support

For support and questions, please contact the development team or create an issue in the repository.
