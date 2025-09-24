-- Bijli Live Database Schema - Complete 5-Level Hierarchical App Support
-- PostgreSQL Database Setup Script with Karachi5846$ password
-- Supports all 7 main modules with full feature scope

-- Create database (run as superuser)
-- CREATE DATABASE bijli_live;
-- \c bijli_live;

-- Set password for postgres user (if needed)
-- ALTER USER postgres PASSWORD 'Karachi5846$';

-- ==============================================
-- CORE USER MANAGEMENT TABLES
-- ==============================================

-- Users table - Enhanced for full app support
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255), -- For future authentication
    profile_photo TEXT,
    bio TEXT,
    location VARCHAR(200),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    date_of_birth DATE,
    gender VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    verification_status VARCHAR(20) DEFAULT 'PENDING' CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    last_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_online BOOLEAN DEFAULT FALSE,
    privacy_settings JSONB DEFAULT '{}',
    notification_settings JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- User verification table
CREATE TABLE IF NOT EXISTS user_verifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    verification_type VARCHAR(20) NOT NULL CHECK (verification_type IN ('EMAIL', 'PHONE', 'ID')),
    verification_code VARCHAR(10),
    verification_token VARCHAR(255),
    expires_at TIMESTAMP NOT NULL,
    verified_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User sessions table
CREATE TABLE IF NOT EXISTS user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    device_type VARCHAR(20) NOT NULL CHECK (device_type IN ('ANDROID', 'IOS', 'WEB', 'DESKTOP')),
    device_name VARCHAR(100),
    device_id VARCHAR(255),
    session_token VARCHAR(255) UNIQUE NOT NULL,
    ip_address INET,
    user_agent TEXT,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- ==============================================
-- CONTACTS & RELATIONSHIPS TABLES
-- ==============================================

-- Contacts table - Enhanced for Plus module
CREATE TABLE IF NOT EXISTS contacts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    contact_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    relationship VARCHAR(20) DEFAULT 'FRIEND' CHECK (relationship IN ('FRIEND', 'FAMILY', 'COLLEAGUE', 'ACQUAINTANCE')),
    is_blocked BOOLEAN DEFAULT FALSE,
    is_favorite BOOLEAN DEFAULT FALSE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, contact_user_id)
);

-- Contact groups table
CREATE TABLE IF NOT EXISTS contact_groups (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    color VARCHAR(7) DEFAULT '#007AFF', -- Hex color
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Contact group members
CREATE TABLE IF NOT EXISTS contact_group_members (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT REFERENCES contact_groups(id) ON DELETE CASCADE,
    contact_id BIGINT REFERENCES contacts(id) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(group_id, contact_id)
);

-- ==============================================
-- CHAT & MESSAGING TABLES (Chats Module)
-- ==============================================

-- Chats table - Enhanced for full chat functionality
CREATE TABLE IF NOT EXISTS chats (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    type VARCHAR(20) DEFAULT 'PRIVATE' CHECK (type IN ('PRIVATE', 'GROUP', 'BROADCAST', 'CHANNEL')),
    created_by BIGINT REFERENCES users(id) ON DELETE CASCADE,
    avatar_url TEXT,
    invite_link VARCHAR(255) UNIQUE,
    invite_expires_at TIMESTAMP,
    max_participants INTEGER DEFAULT 256,
    is_public BOOLEAN DEFAULT FALSE,
    is_archived BOOLEAN DEFAULT FALSE,
    settings JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_message_at TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Chat participants table - Enhanced
CREATE TABLE IF NOT EXISTS chat_participants (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT REFERENCES chats(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    role VARCHAR(20) DEFAULT 'MEMBER' CHECK (role IN ('ADMIN', 'MODERATOR', 'MEMBER')),
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT FALSE,
    is_pinned BOOLEAN DEFAULT FALSE,
    notification_settings JSONB DEFAULT '{}',
    last_read_message_id BIGINT,
    last_read_at TIMESTAMP,
    UNIQUE(chat_id, user_id)
);

-- Messages table - Enhanced for all message types
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT REFERENCES chats(id) ON DELETE CASCADE,
    sender_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    reply_to_message_id BIGINT REFERENCES messages(id) ON DELETE SET NULL,
    content TEXT,
    message_type VARCHAR(20) DEFAULT 'TEXT' CHECK (message_type IN ('TEXT', 'IMAGE', 'VIDEO', 'AUDIO', 'DOCUMENT', 'LOCATION', 'CONTACT', 'STICKER', 'SYSTEM')),
    media_url TEXT,
    media_thumbnail TEXT,
    media_duration INTEGER, -- For audio/video in seconds
    media_size BIGINT, -- File size in bytes
    file_name VARCHAR(255),
    mime_type VARCHAR(100),
    caption TEXT,
    metadata JSONB DEFAULT '{}',
    is_edited BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,
    deleted_at TIMESTAMP,
    deleted_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Message reactions table
CREATE TABLE IF NOT EXISTS message_reactions (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT REFERENCES messages(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    emoji VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(message_id, user_id, emoji)
);

-- Message status table (delivered, read, etc.)
CREATE TABLE IF NOT EXISTS message_status (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT REFERENCES messages(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'SENT' CHECK (status IN ('SENT', 'DELIVERED', 'READ')),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(message_id, user_id)
);

-- Saved messages table
CREATE TABLE IF NOT EXISTS saved_messages (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    message_id BIGINT REFERENCES messages(id) ON DELETE CASCADE,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, message_id)
);

-- Broadcast lists table
CREATE TABLE IF NOT EXISTS broadcast_lists (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Broadcast list recipients
CREATE TABLE IF NOT EXISTS broadcast_recipients (
    id BIGSERIAL PRIMARY KEY,
    broadcast_id BIGINT REFERENCES broadcast_lists(id) ON DELETE CASCADE,
    contact_id BIGINT REFERENCES contacts(id) ON DELETE CASCADE,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(broadcast_id, contact_id)
);

-- ==============================================
-- MARKETPLACE TABLES (Services Module)
-- ==============================================

-- Products table - Enhanced marketplace support
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'USD',
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(50),
    brand VARCHAR(100),
    condition VARCHAR(20) DEFAULT 'NEW' CHECK (condition IN ('NEW', 'LIKE_NEW', 'GOOD', 'FAIR', 'POOR')),
    location VARCHAR(200),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    shipping_cost DECIMAL(10,2) DEFAULT 0,
    shipping_method VARCHAR(50),
    is_negotiable BOOLEAN DEFAULT FALSE,
    is_featured BOOLEAN DEFAULT FALSE,
    views_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    inquiries_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'SOLD', 'RESERVED', 'INACTIVE', 'DELETED')),
    sold_at TIMESTAMP,
    sold_to BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Product images table - Enhanced
CREATE TABLE IF NOT EXISTS product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    thumbnail_url TEXT,
    order_index INTEGER DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    file_size BIGINT,
    width INTEGER,
    height INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Product categories table
CREATE TABLE IF NOT EXISTS product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    parent_id BIGINT REFERENCES product_categories(id) ON DELETE CASCADE,
    icon VARCHAR(100),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0
);

-- Product favorites table
CREATE TABLE IF NOT EXISTS product_favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

-- Product inquiries table
CREATE TABLE IF NOT EXISTS product_inquiries (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    buyer_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    seller_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'REPLIED', 'CLOSED')),
    replied_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Product reviews table
CREATE TABLE IF NOT EXISTS product_reviews (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products(id) ON DELETE CASCADE,
    buyer_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    seller_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    title VARCHAR(200),
    comment TEXT,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- RIDE HAILING TABLES (Services Module)
-- ==============================================

-- Ride requests table - Enhanced
CREATE TABLE IF NOT EXISTS ride_requests (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    driver_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    pickup_location VARCHAR(200) NOT NULL,
    dropoff_location VARCHAR(200) NOT NULL,
    pickup_latitude DECIMAL(10,8) NOT NULL,
    pickup_longitude DECIMAL(11,8) NOT NULL,
    dropoff_latitude DECIMAL(10,8) NOT NULL,
    dropoff_longitude DECIMAL(11,8) NOT NULL,
    ride_type VARCHAR(20) DEFAULT 'ECONOMY' CHECK (ride_type IN ('ECONOMY', 'SEDAN', 'LUXURY', 'SUV', 'MOTORCYCLE')),
    estimated_distance DECIMAL(8,2), -- in kilometers
    estimated_duration INTEGER, -- in minutes
    base_fare DECIMAL(10,2),
    distance_fare DECIMAL(10,2),
    time_fare DECIMAL(10,2),
    surge_multiplier DECIMAL(3,2) DEFAULT 1.0,
    total_fare DECIMAL(10,2),
    payment_method VARCHAR(20) DEFAULT 'CASH' CHECK (payment_method IN ('CASH', 'CARD', 'WALLET')),
    payment_status VARCHAR(20) DEFAULT 'PENDING' CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED')),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACCEPTED', 'ARRIVING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    cancellation_reason TEXT,
    cancelled_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    notes TEXT,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP
);

-- Driver profiles table
CREATE TABLE IF NOT EXISTS driver_profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE UNIQUE,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    license_expiry DATE NOT NULL,
    vehicle_make VARCHAR(50) NOT NULL,
    vehicle_model VARCHAR(50) NOT NULL,
    vehicle_year INTEGER NOT NULL,
    vehicle_color VARCHAR(20) NOT NULL,
    vehicle_plate VARCHAR(20) UNIQUE NOT NULL,
    vehicle_type VARCHAR(20) NOT NULL CHECK (vehicle_type IN ('CAR', 'SUV', 'MOTORCYCLE', 'BICYCLE')),
    max_passengers INTEGER DEFAULT 4,
    is_available BOOLEAN DEFAULT FALSE,
    current_latitude DECIMAL(10,8),
    current_longitude DECIMAL(11,8),
    current_location VARCHAR(200),
    rating DECIMAL(3,2) DEFAULT 5.0,
    total_rides INTEGER DEFAULT 0,
    total_earnings DECIMAL(10,2) DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    verification_documents JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Ride ratings table
CREATE TABLE IF NOT EXISTS ride_ratings (
    id BIGSERIAL PRIMARY KEY,
    ride_id BIGINT REFERENCES ride_requests(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    driver_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    user_rating INTEGER CHECK (user_rating >= 1 AND user_rating <= 5),
    driver_rating INTEGER CHECK (driver_rating >= 1 AND driver_rating <= 5),
    user_comment TEXT,
    driver_comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- FOOD DELIVERY TABLES (Services Module)
-- ==============================================

-- Restaurants table
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    cuisine_type VARCHAR(50),
    address TEXT NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(255),
    logo_url TEXT,
    cover_image_url TEXT,
    rating DECIMAL(3,2) DEFAULT 0.0,
    total_orders INTEGER DEFAULT 0,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    minimum_order DECIMAL(10,2) DEFAULT 0,
    estimated_delivery_time INTEGER DEFAULT 30, -- in minutes
    is_open BOOLEAN DEFAULT TRUE,
    opening_hours JSONB DEFAULT '{}',
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu items table
CREATE TABLE IF NOT EXISTS menu_items (
    id BIGSERIAL PRIMARY KEY,
    restaurant_id BIGINT REFERENCES restaurants(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    category VARCHAR(50),
    image_url TEXT,
    is_vegetarian BOOLEAN DEFAULT FALSE,
    is_vegan BOOLEAN DEFAULT FALSE,
    is_gluten_free BOOLEAN DEFAULT FALSE,
    is_spicy BOOLEAN DEFAULT FALSE,
    spice_level INTEGER DEFAULT 0 CHECK (spice_level >= 0 AND spice_level <= 5),
    preparation_time INTEGER DEFAULT 15, -- in minutes
    is_available BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Food orders table - Enhanced
CREATE TABLE IF NOT EXISTS food_orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    restaurant_id BIGINT REFERENCES restaurants(id) ON DELETE CASCADE,
    order_number VARCHAR(20) UNIQUE NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    delivery_fee DECIMAL(10,2) DEFAULT 0,
    service_fee DECIMAL(10,2) DEFAULT 0,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    discount_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(20) DEFAULT 'CASH' CHECK (payment_method IN ('CASH', 'CARD', 'WALLET')),
    payment_status VARCHAR(20) DEFAULT 'PENDING' CHECK (payment_status IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED')),
    delivery_address TEXT NOT NULL,
    delivery_latitude DECIMAL(10,8),
    delivery_longitude DECIMAL(11,8),
    delivery_instructions TEXT,
    estimated_delivery_time TIMESTAMP,
    actual_delivery_time TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'PREPARING', 'READY', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED')),
    cancellation_reason TEXT,
    notes TEXT,
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    prepared_at TIMESTAMP,
    out_for_delivery_at TIMESTAMP,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP
);

-- Order items table - Enhanced
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES food_orders(id) ON DELETE CASCADE,
    menu_item_id BIGINT REFERENCES menu_items(id) ON DELETE CASCADE,
    item_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Delivery drivers table
CREATE TABLE IF NOT EXISTS delivery_drivers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE UNIQUE,
    vehicle_type VARCHAR(20) NOT NULL CHECK (vehicle_type IN ('MOTORCYCLE', 'BICYCLE', 'CAR', 'WALKING')),
    vehicle_number VARCHAR(20),
    is_available BOOLEAN DEFAULT FALSE,
    current_latitude DECIMAL(10,8),
    current_longitude DECIMAL(11,8),
    rating DECIMAL(3,2) DEFAULT 5.0,
    total_deliveries INTEGER DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order assignments table
CREATE TABLE IF NOT EXISTS order_assignments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES food_orders(id) ON DELETE CASCADE,
    driver_id BIGINT REFERENCES delivery_drivers(id) ON DELETE SET NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    picked_up_at TIMESTAMP,
    delivered_at TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ASSIGNED' CHECK (status IN ('ASSIGNED', 'PICKED_UP', 'DELIVERED', 'CANCELLED'))
);

-- ==============================================
-- REAL ESTATE TABLES (Services Module)
-- ==============================================

-- Properties table
CREATE TABLE IF NOT EXISTS properties (
    id BIGSERIAL PRIMARY KEY,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    property_type VARCHAR(50) NOT NULL CHECK (property_type IN ('APARTMENT', 'HOUSE', 'CONDO', 'TOWNHOUSE', 'STUDIO', 'COMMERCIAL', 'LAND')),
    listing_type VARCHAR(20) NOT NULL CHECK (listing_type IN ('SALE', 'RENT', 'BOTH')),
    price DECIMAL(12,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    area DECIMAL(10,2), -- in square feet/meters
    bedrooms INTEGER DEFAULT 0,
    bathrooms INTEGER DEFAULT 0,
    floors INTEGER DEFAULT 1,
    year_built INTEGER,
    address TEXT NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    country VARCHAR(100) DEFAULT 'USA',
    zip_code VARCHAR(20),
    amenities JSONB DEFAULT '[]',
    features JSONB DEFAULT '[]',
    is_furnished BOOLEAN DEFAULT FALSE,
    is_pet_friendly BOOLEAN DEFAULT FALSE,
    parking_spaces INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    inquiries_count INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'SOLD', 'RENTED', 'INACTIVE', 'DELETED')),
    sold_at TIMESTAMP,
    sold_to BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Property images table
CREATE TABLE IF NOT EXISTS property_images (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT REFERENCES properties(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    thumbnail_url TEXT,
    order_index INTEGER DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    room_type VARCHAR(50), -- LIVING_ROOM, BEDROOM, KITCHEN, BATHROOM, etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Property inquiries table
CREATE TABLE IF NOT EXISTS property_inquiries (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT REFERENCES properties(id) ON DELETE CASCADE,
    buyer_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    inquiry_type VARCHAR(20) DEFAULT 'GENERAL' CHECK (inquiry_type IN ('GENERAL', 'VIEWING', 'PRICE', 'AVAILABILITY')),
    message TEXT NOT NULL,
    preferred_contact_method VARCHAR(20) DEFAULT 'PHONE' CHECK (preferred_contact_method IN ('PHONE', 'EMAIL', 'SMS')),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'REPLIED', 'SCHEDULED', 'CLOSED')),
    replied_at TIMESTAMP,
    scheduled_viewing_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- TRANSACTION & PAYMENT TABLES (Me Module)
-- ==============================================

-- Transactions table - Enhanced
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    transaction_id VARCHAR(50) UNIQUE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    type VARCHAR(30) NOT NULL CHECK (type IN ('PAYMENT', 'REFUND', 'RIDE_PAYMENT', 'FOOD_ORDER', 'MARKETPLACE_PURCHASE', 'PROPERTY_PURCHASE', 'WALLET_TOPUP', 'WITHDRAWAL', 'TRANSFER')),
    description TEXT NOT NULL,
    reference_id VARCHAR(100),
    reference_type VARCHAR(50), -- RIDE_REQUEST, FOOD_ORDER, PRODUCT, etc.
    payment_method VARCHAR(20) DEFAULT 'CASH' CHECK (payment_method IN ('CASH', 'CARD', 'WALLET', 'BANK_TRANSFER', 'CRYPTO')),
    payment_gateway VARCHAR(50),
    gateway_transaction_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED', 'REFUNDED')),
    failure_reason TEXT,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    failed_at TIMESTAMP
);

-- User wallet table
CREATE TABLE IF NOT EXISTS user_wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE UNIQUE,
    balance DECIMAL(12,2) DEFAULT 0,
    currency VARCHAR(3) DEFAULT 'USD',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Wallet transactions table
CREATE TABLE IF NOT EXISTS wallet_transactions (
    id BIGSERIAL PRIMARY KEY,
    wallet_id BIGINT REFERENCES user_wallets(id) ON DELETE CASCADE,
    transaction_id BIGINT REFERENCES transactions(id) ON DELETE CASCADE,
    amount DECIMAL(12,2) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('CREDIT', 'DEBIT')),
    description TEXT,
    balance_after DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- SOCIAL MEDIA TABLES (Discover Module)
-- ==============================================

-- Posts table - Enhanced
CREATE TABLE IF NOT EXISTS posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    post_type VARCHAR(20) DEFAULT 'TEXT' CHECK (post_type IN ('TEXT', 'IMAGE', 'VIDEO', 'MOMENT', 'LINK', 'POLL')),
    is_public BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE,
    location VARCHAR(200),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    tags TEXT[], -- Array of hashtags
    mentions BIGINT[], -- Array of user IDs mentioned
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    shares_count INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    is_edited BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post media table - Enhanced
CREATE TABLE IF NOT EXISTS post_media (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    media_url TEXT NOT NULL,
    thumbnail_url TEXT,
    media_type VARCHAR(20) NOT NULL CHECK (media_type IN ('IMAGE', 'VIDEO', 'AUDIO', 'GIF')),
    order_index INTEGER DEFAULT 0,
    file_size BIGINT,
    duration INTEGER, -- For video/audio in seconds
    width INTEGER,
    height INTEGER,
    caption TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post likes table
CREATE TABLE IF NOT EXISTS post_likes (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    reaction_type VARCHAR(10) DEFAULT 'LIKE' CHECK (reaction_type IN ('LIKE', 'LOVE', 'LAUGH', 'SAD', 'ANGRY')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(post_id, user_id)
);

-- Post comments table - Enhanced
CREATE TABLE IF NOT EXISTS post_comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    parent_comment_id BIGINT REFERENCES post_comments(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    likes_count INTEGER DEFAULT 0,
    is_edited BOOLEAN DEFAULT FALSE,
    edited_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post shares table
CREATE TABLE IF NOT EXISTS post_shares (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    share_type VARCHAR(20) DEFAULT 'SHARE' CHECK (share_type IN ('SHARE', 'REPOST', 'QUOTE')),
    shared_content TEXT, -- For quote tweets
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Post reports table
CREATE TABLE IF NOT EXISTS post_reports (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    reporter_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    reason VARCHAR(50) NOT NULL CHECK (reason IN ('SPAM', 'INAPPROPRIATE', 'HARASSMENT', 'FALSE_INFO', 'COPYRIGHT', 'OTHER')),
    description TEXT,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'REVIEWED', 'RESOLVED', 'DISMISSED')),
    reviewed_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- CAMERA & MEDIA TABLES (Camera Module)
-- ==============================================

-- Media files table
CREATE TABLE IF NOT EXISTS media_files (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    original_name VARCHAR(255),
    file_path TEXT NOT NULL,
    file_url TEXT,
    thumbnail_url TEXT,
    file_type VARCHAR(20) NOT NULL CHECK (file_type IN ('IMAGE', 'VIDEO', 'AUDIO', 'DOCUMENT')),
    mime_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    width INTEGER,
    height INTEGER,
    duration INTEGER, -- For video/audio in seconds
    metadata JSONB DEFAULT '{}',
    is_processed BOOLEAN DEFAULT FALSE,
    processing_status VARCHAR(20) DEFAULT 'PENDING' CHECK (processing_status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Photo sessions table
CREATE TABLE IF NOT EXISTS photo_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    session_name VARCHAR(200),
    skin_tone VARCHAR(20) DEFAULT 'DEFAULT' CHECK (skin_tone IN ('DEFAULT', 'LIGHT', 'MEDIUM', 'DARK')),
    filter_applied VARCHAR(50),
    caption TEXT,
    location VARCHAR(200),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    is_shared BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Photo session media
CREATE TABLE IF NOT EXISTS photo_session_media (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT REFERENCES photo_sessions(id) ON DELETE CASCADE,
    media_id BIGINT REFERENCES media_files(id) ON DELETE CASCADE,
    order_index INTEGER DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Video sessions table
CREATE TABLE IF NOT EXISTS video_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    session_name VARCHAR(200),
    original_duration INTEGER NOT NULL, -- in seconds
    trimmed_duration INTEGER,
    trim_start INTEGER DEFAULT 0, -- start time in seconds
    trim_end INTEGER, -- end time in seconds
    auto_shorten BOOLEAN DEFAULT FALSE,
    preset_duration INTEGER, -- 15s, 30s, 60s presets
    caption TEXT,
    location VARCHAR(200),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    is_shared BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Video session media
CREATE TABLE IF NOT EXISTS video_session_media (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT REFERENCES video_sessions(id) ON DELETE CASCADE,
    media_id BIGINT REFERENCES media_files(id) ON DELETE CASCADE,
    is_original BOOLEAN DEFAULT TRUE,
    is_trimmed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Group call sessions table
CREATE TABLE IF NOT EXISTS group_call_sessions (
    id BIGSERIAL PRIMARY KEY,
    created_by BIGINT REFERENCES users(id) ON DELETE CASCADE,
    chat_id BIGINT REFERENCES chats(id) ON DELETE CASCADE,
    session_name VARCHAR(200),
    invite_code VARCHAR(20) UNIQUE NOT NULL,
    qr_code_url TEXT,
    invite_link VARCHAR(255) UNIQUE,
    expires_at TIMESTAMP,
    max_participants INTEGER DEFAULT 8,
    is_active BOOLEAN DEFAULT TRUE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Group call participants
CREATE TABLE IF NOT EXISTS group_call_participants (
    id BIGSERIAL PRIMARY KEY,
    call_id BIGINT REFERENCES group_call_sessions(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT FALSE,
    is_video_enabled BOOLEAN DEFAULT TRUE,
    UNIQUE(call_id, user_id)
);

-- ==============================================
-- SEARCH & DISCOVERY TABLES (Search Module)
-- ==============================================

-- Search history table
CREATE TABLE IF NOT EXISTS search_history (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    search_query TEXT NOT NULL,
    search_type VARCHAR(20) NOT NULL CHECK (search_type IN ('CHAT', 'MARKETPLACE', 'USER', 'POST', 'GENERAL')),
    results_count INTEGER DEFAULT 0,
    clicked_result_id BIGINT,
    clicked_result_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Search suggestions table
CREATE TABLE IF NOT EXISTS search_suggestions (
    id BIGSERIAL PRIMARY KEY,
    suggestion_text TEXT NOT NULL,
    suggestion_type VARCHAR(20) NOT NULL CHECK (suggestion_type IN ('CHAT', 'MARKETPLACE', 'USER', 'POST', 'GENERAL')),
    usage_count INTEGER DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- NOTIFICATIONS & ALERTS TABLES
-- ==============================================

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(30) NOT NULL CHECK (type IN ('MESSAGE', 'FRIEND_REQUEST', 'POST_LIKE', 'POST_COMMENT', 'RIDE_UPDATE', 'ORDER_UPDATE', 'PAYMENT', 'SYSTEM')),
    reference_id BIGINT,
    reference_type VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
    is_actionable BOOLEAN DEFAULT FALSE,
    action_url TEXT,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP
);

-- Notification preferences table
CREATE TABLE IF NOT EXISTS notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE UNIQUE,
    email_notifications BOOLEAN DEFAULT TRUE,
    push_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    message_notifications BOOLEAN DEFAULT TRUE,
    post_notifications BOOLEAN DEFAULT TRUE,
    service_notifications BOOLEAN DEFAULT TRUE,
    marketing_notifications BOOLEAN DEFAULT FALSE,
    quiet_hours_start TIME,
    quiet_hours_end TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- SUPPORT & HELP TABLES
-- ==============================================

-- Support tickets table
CREATE TABLE IF NOT EXISTS support_tickets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    ticket_number VARCHAR(20) UNIQUE NOT NULL,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('ACCOUNT', 'PAYMENT', 'TECHNICAL', 'FEATURE_REQUEST', 'BUG_REPORT', 'GENERAL')),
    priority VARCHAR(20) DEFAULT 'MEDIUM' CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    status VARCHAR(20) DEFAULT 'OPEN' CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    assigned_to BIGINT REFERENCES users(id) ON DELETE SET NULL,
    resolution TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    closed_at TIMESTAMP
);

-- Support ticket messages
CREATE TABLE IF NOT EXISTS support_ticket_messages (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT REFERENCES support_tickets(id) ON DELETE CASCADE,
    sender_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    message TEXT NOT NULL,
    is_internal BOOLEAN DEFAULT FALSE,
    attachments JSONB DEFAULT '[]',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- FAQ table
CREATE TABLE IF NOT EXISTS faqs (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    order_index INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    views_count INTEGER DEFAULT 0,
    helpful_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- STICKERS & EMOJI TABLES (Me Module)
-- ==============================================

-- Sticker packs table
CREATE TABLE IF NOT EXISTS sticker_packs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    pack_type VARCHAR(20) DEFAULT 'EMOJI' CHECK (pack_type IN ('EMOJI', 'ANIMATED', 'STATIC', 'CUSTOM')),
    price DECIMAL(10,2) DEFAULT 0,
    currency VARCHAR(3) DEFAULT 'USD',
    is_free BOOLEAN DEFAULT TRUE,
    is_featured BOOLEAN DEFAULT FALSE,
    download_count INTEGER DEFAULT 0,
    preview_url TEXT,
    thumbnail_url TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Stickers table
CREATE TABLE IF NOT EXISTS stickers (
    id BIGSERIAL PRIMARY KEY,
    pack_id BIGINT REFERENCES sticker_packs(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    emoji VARCHAR(10),
    image_url TEXT NOT NULL,
    animated_url TEXT,
    order_index INTEGER DEFAULT 0,
    usage_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User sticker packs
CREATE TABLE IF NOT EXISTS user_sticker_packs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    pack_id BIGINT REFERENCES sticker_packs(id) ON DELETE CASCADE,
    purchased_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, pack_id)
);

-- ==============================================
-- COUPONS & OFFERS TABLES (Me Module)
-- ==============================================

-- Coupons table
CREATE TABLE IF NOT EXISTS coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    discount_type VARCHAR(20) NOT NULL CHECK (discount_type IN ('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING')),
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order_amount DECIMAL(10,2) DEFAULT 0,
    maximum_discount DECIMAL(10,2),
    usage_limit INTEGER,
    usage_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    valid_from TIMESTAMP NOT NULL,
    valid_until TIMESTAMP NOT NULL,
    applicable_categories TEXT[],
    applicable_products BIGINT[],
    created_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User coupon usage
CREATE TABLE IF NOT EXISTS user_coupon_usage (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    coupon_id BIGINT REFERENCES coupons(id) ON DELETE CASCADE,
    order_id BIGINT,
    order_type VARCHAR(50),
    discount_amount DECIMAL(10,2) NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, coupon_id, order_id)
);

-- ==============================================
-- COMPREHENSIVE INDEXES FOR PERFORMANCE
-- ==============================================

-- User management indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_phone ON users(phone);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_location ON users(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_user_sessions_user_id ON user_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_user_sessions_token ON user_sessions(session_token);
CREATE INDEX IF NOT EXISTS idx_user_verifications_user_id ON user_verifications(user_id);

-- Contact management indexes
CREATE INDEX IF NOT EXISTS idx_contacts_user_id ON contacts(user_id);
CREATE INDEX IF NOT EXISTS idx_contacts_contact_user_id ON contacts(contact_user_id);
CREATE INDEX IF NOT EXISTS idx_contacts_blocked ON contacts(is_blocked);
CREATE INDEX IF NOT EXISTS idx_contact_groups_user_id ON contact_groups(user_id);

-- Chat and messaging indexes
CREATE INDEX IF NOT EXISTS idx_chats_type ON chats(type);
CREATE INDEX IF NOT EXISTS idx_chats_created_by ON chats(created_by);
CREATE INDEX IF NOT EXISTS idx_chats_last_message ON chats(last_message_at);
CREATE INDEX IF NOT EXISTS idx_chat_participants_chat_id ON chat_participants(chat_id);
CREATE INDEX IF NOT EXISTS idx_chat_participants_user_id ON chat_participants(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_participants_role ON chat_participants(role);
CREATE INDEX IF NOT EXISTS idx_messages_chat_id ON messages(chat_id);
CREATE INDEX IF NOT EXISTS idx_messages_sender_id ON messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_messages_created_at ON messages(created_at);
CREATE INDEX IF NOT EXISTS idx_messages_type ON messages(message_type);
CREATE INDEX IF NOT EXISTS idx_message_reactions_message_id ON message_reactions(message_id);
CREATE INDEX IF NOT EXISTS idx_message_status_message_id ON message_status(message_id);
CREATE INDEX IF NOT EXISTS idx_saved_messages_user_id ON saved_messages(user_id);

-- Marketplace indexes
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);
CREATE INDEX IF NOT EXISTS idx_products_subcategory ON products(subcategory);
CREATE INDEX IF NOT EXISTS idx_products_seller_id ON products(seller_id);
CREATE INDEX IF NOT EXISTS idx_products_location ON products(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_products_status ON products(status);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_products_created_at ON products(created_at);
CREATE INDEX IF NOT EXISTS idx_product_images_product_id ON product_images(product_id);
CREATE INDEX IF NOT EXISTS idx_product_favorites_user_id ON product_favorites(user_id);
CREATE INDEX IF NOT EXISTS idx_product_inquiries_product_id ON product_inquiries(product_id);
CREATE INDEX IF NOT EXISTS idx_product_reviews_product_id ON product_reviews(product_id);

-- Ride hailing indexes
CREATE INDEX IF NOT EXISTS idx_ride_requests_user_id ON ride_requests(user_id);
CREATE INDEX IF NOT EXISTS idx_ride_requests_driver_id ON ride_requests(driver_id);
CREATE INDEX IF NOT EXISTS idx_ride_requests_status ON ride_requests(status);
CREATE INDEX IF NOT EXISTS idx_ride_requests_location ON ride_requests(pickup_latitude, pickup_longitude);
CREATE INDEX IF NOT EXISTS idx_ride_requests_created_at ON ride_requests(requested_at);
CREATE INDEX IF NOT EXISTS idx_driver_profiles_user_id ON driver_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_driver_profiles_available ON driver_profiles(is_available);
CREATE INDEX IF NOT EXISTS idx_driver_profiles_location ON driver_profiles(current_latitude, current_longitude);
CREATE INDEX IF NOT EXISTS idx_ride_ratings_ride_id ON ride_ratings(ride_id);

-- Food delivery indexes
CREATE INDEX IF NOT EXISTS idx_restaurants_location ON restaurants(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_restaurants_cuisine ON restaurants(cuisine_type);
CREATE INDEX IF NOT EXISTS idx_restaurants_open ON restaurants(is_open);
CREATE INDEX IF NOT EXISTS idx_menu_items_restaurant_id ON menu_items(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_menu_items_category ON menu_items(category);
CREATE INDEX IF NOT EXISTS idx_food_orders_user_id ON food_orders(user_id);
CREATE INDEX IF NOT EXISTS idx_food_orders_restaurant_id ON food_orders(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_food_orders_status ON food_orders(status);
CREATE INDEX IF NOT EXISTS idx_food_orders_created_at ON food_orders(ordered_at);
CREATE INDEX IF NOT EXISTS idx_delivery_drivers_available ON delivery_drivers(is_available);
CREATE INDEX IF NOT EXISTS idx_delivery_drivers_location ON delivery_drivers(current_latitude, current_longitude);

-- Real estate indexes
CREATE INDEX IF NOT EXISTS idx_properties_type ON properties(property_type);
CREATE INDEX IF NOT EXISTS idx_properties_listing_type ON properties(listing_type);
CREATE INDEX IF NOT EXISTS idx_properties_location ON properties(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_properties_city ON properties(city);
CREATE INDEX IF NOT EXISTS idx_properties_price ON properties(price);
CREATE INDEX IF NOT EXISTS idx_properties_status ON properties(status);
CREATE INDEX IF NOT EXISTS idx_property_images_property_id ON property_images(property_id);
CREATE INDEX IF NOT EXISTS idx_property_inquiries_property_id ON property_inquiries(property_id);

-- Transaction indexes
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(type);
CREATE INDEX IF NOT EXISTS idx_transactions_status ON transactions(status);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);
CREATE INDEX IF NOT EXISTS idx_transactions_reference ON transactions(reference_id, reference_type);
CREATE INDEX IF NOT EXISTS idx_user_wallets_user_id ON user_wallets(user_id);
CREATE INDEX IF NOT EXISTS idx_wallet_transactions_wallet_id ON wallet_transactions(wallet_id);

-- Social media indexes
CREATE INDEX IF NOT EXISTS idx_posts_user_id ON posts(user_id);
CREATE INDEX IF NOT EXISTS idx_posts_type ON posts(post_type);
CREATE INDEX IF NOT EXISTS idx_posts_created_at ON posts(created_at);
CREATE INDEX IF NOT EXISTS idx_posts_public ON posts(is_public);
CREATE INDEX IF NOT EXISTS idx_posts_location ON posts(latitude, longitude);
CREATE INDEX IF NOT EXISTS idx_post_media_post_id ON post_media(post_id);
CREATE INDEX IF NOT EXISTS idx_post_likes_post_id ON post_likes(post_id);
CREATE INDEX IF NOT EXISTS idx_post_comments_post_id ON post_comments(post_id);
CREATE INDEX IF NOT EXISTS idx_post_shares_post_id ON post_shares(post_id);
CREATE INDEX IF NOT EXISTS idx_post_reports_post_id ON post_reports(post_id);

-- Camera and media indexes
CREATE INDEX IF NOT EXISTS idx_media_files_user_id ON media_files(user_id);
CREATE INDEX IF NOT EXISTS idx_media_files_type ON media_files(file_type);
CREATE INDEX IF NOT EXISTS idx_media_files_created_at ON media_files(created_at);
CREATE INDEX IF NOT EXISTS idx_photo_sessions_user_id ON photo_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_video_sessions_user_id ON video_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_group_call_sessions_created_by ON group_call_sessions(created_by);
CREATE INDEX IF NOT EXISTS idx_group_call_sessions_chat_id ON group_call_sessions(chat_id);

-- Search indexes
CREATE INDEX IF NOT EXISTS idx_search_history_user_id ON search_history(user_id);
CREATE INDEX IF NOT EXISTS idx_search_history_type ON search_history(search_type);
CREATE INDEX IF NOT EXISTS idx_search_suggestions_type ON search_suggestions(suggestion_type);

-- Notification indexes
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_read ON notifications(is_read);
CREATE INDEX IF NOT EXISTS idx_notifications_created_at ON notifications(created_at);
CREATE INDEX IF NOT EXISTS idx_notification_preferences_user_id ON notification_preferences(user_id);

-- Support indexes
CREATE INDEX IF NOT EXISTS idx_support_tickets_user_id ON support_tickets(user_id);
CREATE INDEX IF NOT EXISTS idx_support_tickets_status ON support_tickets(status);
CREATE INDEX IF NOT EXISTS idx_support_tickets_category ON support_tickets(category);
CREATE INDEX IF NOT EXISTS idx_support_ticket_messages_ticket_id ON support_ticket_messages(ticket_id);
CREATE INDEX IF NOT EXISTS idx_faqs_category ON faqs(category);

-- Sticker indexes
CREATE INDEX IF NOT EXISTS idx_sticker_packs_type ON sticker_packs(pack_type);
CREATE INDEX IF NOT EXISTS idx_sticker_packs_active ON sticker_packs(is_active);
CREATE INDEX IF NOT EXISTS idx_stickers_pack_id ON stickers(pack_id);
CREATE INDEX IF NOT EXISTS idx_user_sticker_packs_user_id ON user_sticker_packs(user_id);

-- Coupon indexes
CREATE INDEX IF NOT EXISTS idx_coupons_code ON coupons(code);
CREATE INDEX IF NOT EXISTS idx_coupons_active ON coupons(is_active);
CREATE INDEX IF NOT EXISTS idx_coupons_validity ON coupons(valid_from, valid_until);
CREATE INDEX IF NOT EXISTS idx_user_coupon_usage_user_id ON user_coupon_usage(user_id);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_posts_updated_at BEFORE UPDATE ON posts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ==============================================
-- COMPREHENSIVE SAMPLE DATA FOR ALL MODULES
-- ==============================================

-- Insert sample users with enhanced data
INSERT INTO users (username, email, phone, profile_photo, bio, location, latitude, longitude, verification_status, is_online) VALUES
('john_doe', 'john@bijli.live', '+1234567890', 'https://bijli.live/profiles/john.jpg', 'Tech enthusiast and photographer', 'New York, NY', 40.7128, -74.0060, 'VERIFIED', true),
('jane_smith', 'jane@bijli.live', '+1234567891', 'https://bijli.live/profiles/jane.jpg', 'Food lover and travel blogger', 'Los Angeles, CA', 34.0522, -118.2437, 'VERIFIED', true),
('mike_wilson', 'mike@bijli.live', '+1234567892', 'https://bijli.live/profiles/mike.jpg', 'Ride sharing driver and entrepreneur', 'Chicago, IL', 41.8781, -87.6298, 'VERIFIED', false),
('sarah_jones', 'sarah@bijli.live', '+1234567893', 'https://bijli.live/profiles/sarah.jpg', 'Real estate agent and property investor', 'Miami, FL', 25.7617, -80.1918, 'VERIFIED', true),
('alex_brown', 'alex@bijli.live', '+1234567894', 'https://bijli.live/profiles/alex.jpg', 'Restaurant owner and chef', 'Seattle, WA', 47.6062, -122.3321, 'VERIFIED', false)
ON CONFLICT (email) DO NOTHING;

-- Insert user wallets
INSERT INTO user_wallets (user_id, balance, currency) VALUES
(1, 150.00, 'USD'),
(2, 75.50, 'USD'),
(3, 200.00, 'USD'),
(4, 500.00, 'USD'),
(5, 125.75, 'USD')
ON CONFLICT (user_id) DO NOTHING;

-- Insert product categories
INSERT INTO product_categories (name, parent_id, icon, description) VALUES
('Electronics', NULL, '📱', 'Electronic devices and gadgets'),
('Fashion', NULL, '👕', 'Clothing and accessories'),
('Home & Garden', NULL, '🏠', 'Home improvement and gardening'),
('Sports', NULL, '⚽', 'Sports equipment and fitness'),
('Books', NULL, '📚', 'Books and educational materials'),
('Mobiles', 1, '📱', 'Mobile phones and accessories'),
('Laptops', 1, '💻', 'Laptops and computers'),
('Accessories', 1, '🎧', 'Electronic accessories'),
('Appliances', 1, '🏠', 'Home appliances'),
('Men''s Wear', 2, '👔', 'Men''s clothing'),
('Women''s Wear', 2, '👗', 'Women''s clothing'),
('Kids'' Wear', 2, '👶', 'Children''s clothing'),
('Accessories', 2, '👜', 'Fashion accessories')
ON CONFLICT (name) DO NOTHING;

-- Insert sample products
INSERT INTO products (seller_id, title, description, price, original_price, category, subcategory, brand, condition, location, latitude, longitude, views_count, likes_count) VALUES
(1, 'iPhone 15 Pro Max', 'Latest iPhone with titanium design and advanced camera system', 1199.99, 1299.99, 'Electronics', 'Mobiles', 'Apple', 'NEW', 'New York, NY', 40.7128, -74.0060, 1250, 45),
(1, 'MacBook Pro M3', 'Powerful laptop for professionals with M3 chip', 1999.99, 2199.99, 'Electronics', 'Laptops', 'Apple', 'NEW', 'New York, NY', 40.7128, -74.0060, 890, 32),
(2, 'Designer Handbag', 'Luxury leather handbag from premium brand', 299.99, 399.99, 'Fashion', 'Accessories', 'Gucci', 'LIKE_NEW', 'Los Angeles, CA', 34.0522, -118.2437, 567, 23),
(2, 'Summer Dress Collection', 'Beautiful summer dresses for all occasions', 89.99, 120.00, 'Fashion', 'Women''s Wear', 'Zara', 'NEW', 'Los Angeles, CA', 34.0522, -118.2437, 445, 18),
(3, 'Gaming Laptop', 'High-performance gaming laptop with RTX graphics', 1499.99, 1799.99, 'Electronics', 'Laptops', 'ASUS', 'GOOD', 'Chicago, IL', 41.8781, -87.6298, 678, 28)
ON CONFLICT DO NOTHING;

-- Insert product images
INSERT INTO product_images (product_id, image_url, thumbnail_url, order_index, is_primary) VALUES
(1, 'https://bijli.live/products/iphone15_1.jpg', 'https://bijli.live/products/thumbnails/iphone15_1.jpg', 0, true),
(1, 'https://bijli.live/products/iphone15_2.jpg', 'https://bijli.live/products/thumbnails/iphone15_2.jpg', 1, false),
(2, 'https://bijli.live/products/macbook_1.jpg', 'https://bijli.live/products/thumbnails/macbook_1.jpg', 0, true),
(3, 'https://bijli.live/products/handbag_1.jpg', 'https://bijli.live/products/thumbnails/handbag_1.jpg', 0, true),
(4, 'https://bijli.live/products/dress_1.jpg', 'https://bijli.live/products/thumbnails/dress_1.jpg', 0, true),
(5, 'https://bijli.live/products/gaming_laptop_1.jpg', 'https://bijli.live/products/thumbnails/gaming_laptop_1.jpg', 0, true)
ON CONFLICT DO NOTHING;

-- Insert sample restaurants
INSERT INTO restaurants (owner_id, name, description, cuisine_type, address, latitude, longitude, phone, rating, delivery_fee, minimum_order, estimated_delivery_time) VALUES
(5, 'Pizza Palace', 'Authentic Italian pizza with fresh ingredients', 'Italian', '123 Main St, Seattle, WA', 47.6062, -122.3321, '+12065551234', 4.5, 3.99, 15.00, 25),
(5, 'Burger King', 'Classic American burgers and fries', 'American', '456 Oak Ave, Seattle, WA', 47.6062, -122.3321, '+12065551235', 4.2, 2.99, 10.00, 20),
(5, 'Sushi Master', 'Fresh sushi and Japanese cuisine', 'Japanese', '789 Pine St, Seattle, WA', 47.6062, -122.3321, '+12065551236', 4.8, 4.99, 20.00, 30)
ON CONFLICT DO NOTHING;

-- Insert menu items
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_vegetarian, is_spicy, spice_level) VALUES
(1, 'Margherita Pizza', 'Classic pizza with tomato sauce, mozzarella, and basil', 12.99, 'Pizza', false, false, 0),
(1, 'Pepperoni Pizza', 'Pizza with pepperoni and mozzarella cheese', 14.99, 'Pizza', false, false, 0),
(1, 'Vegetarian Pizza', 'Pizza with vegetables and cheese', 13.99, 'Pizza', true, false, 0),
(2, 'Classic Burger', 'Beef burger with lettuce, tomato, and onion', 8.99, 'Burgers', false, false, 0),
(2, 'Spicy Chicken Burger', 'Chicken burger with spicy sauce', 9.99, 'Burgers', false, true, 3),
(3, 'California Roll', 'Crab, avocado, and cucumber roll', 6.99, 'Sushi', false, false, 0),
(3, 'Spicy Tuna Roll', 'Tuna with spicy mayo', 8.99, 'Sushi', false, true, 2)
ON CONFLICT DO NOTHING;

-- Insert sample chats
INSERT INTO chats (name, description, type, created_by, avatar_url, invite_link, max_participants) VALUES
('General Chat', 'Main group chat for everyone', 'GROUP', 1, 'https://bijli.live/chats/general.jpg', 'bijli.live/join/general123', 100),
('Tech Discussion', 'Technology and programming discussions', 'GROUP', 2, 'https://bijli.live/chats/tech.jpg', 'bijli.live/join/tech456', 50),
('Food Lovers', 'Share food photos and recipes', 'GROUP', 5, 'https://bijli.live/chats/food.jpg', 'bijli.live/join/food789', 75),
('Ride Sharing', 'Coordinate rides and share experiences', 'GROUP', 3, 'https://bijli.live/chats/rides.jpg', 'bijli.live/join/rides012', 30)
ON CONFLICT DO NOTHING;

-- Add participants to chats
INSERT INTO chat_participants (chat_id, user_id, role, is_pinned) VALUES
(1, 1, 'ADMIN', true),
(1, 2, 'MEMBER', false),
(1, 3, 'MEMBER', false),
(1, 4, 'MEMBER', false),
(1, 5, 'MEMBER', false),
(2, 1, 'MEMBER', false),
(2, 2, 'ADMIN', true),
(2, 3, 'MEMBER', false),
(3, 2, 'MEMBER', false),
(3, 5, 'ADMIN', true),
(4, 1, 'MEMBER', false),
(4, 3, 'ADMIN', true)
ON CONFLICT DO NOTHING;

-- Insert sample messages
INSERT INTO messages (chat_id, sender_id, content, message_type, created_at) VALUES
(1, 1, 'Welcome to Bijli Live! 🎉', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(1, 2, 'Thanks for the invite! Excited to be here', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '1 hour 45 minutes'),
(1, 3, 'Hey everyone! 👋', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes'),
(2, 2, 'Anyone interested in the new iPhone 15 Pro?', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '3 hours'),
(2, 1, 'Yes! The camera looks amazing 📸', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '2 hours 45 minutes'),
(3, 5, 'Check out this amazing pizza I made! 🍕', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '4 hours'),
(3, 2, 'That looks delicious! Recipe please?', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '3 hours 30 minutes'),
(4, 3, 'Available for rides in downtown area', 'TEXT', CURRENT_TIMESTAMP - INTERVAL '5 hours')
ON CONFLICT DO NOTHING;

-- Insert sample posts
INSERT INTO posts (user_id, content, post_type, location, latitude, longitude, tags, likes_count, comments_count, shares_count) VALUES
(1, 'Beautiful sunset today! 🌅 Perfect weather for photography', 'MOMENT', 'Central Park, NYC', 40.7829, -73.9654, ARRAY['sunset', 'photography', 'nature'], 15, 3, 2),
(2, 'Just finished my workout 💪 Feeling energized!', 'MOMENT', 'LA Fitness, LA', 34.0522, -118.2437, ARRAY['workout', 'fitness', 'motivation'], 8, 1, 0),
(3, 'Coffee break ☕ Best way to start the day', 'MOMENT', 'Starbucks, Chicago', 41.8781, -87.6298, ARRAY['coffee', 'morning', 'break'], 5, 0, 1),
(4, 'New property listing! 3BR/2BA condo in Miami Beach', 'TEXT', 'Miami Beach, FL', 25.7907, -80.1300, ARRAY['realestate', 'miami', 'condo'], 12, 5, 3),
(5, 'Fresh sushi rolls ready! 🍣 Who wants some?', 'MOMENT', 'Sushi Master, Seattle', 47.6062, -122.3321, ARRAY['sushi', 'food', 'restaurant'], 20, 8, 4)
ON CONFLICT DO NOTHING;

-- Insert sample transactions
INSERT INTO transactions (user_id, transaction_id, amount, type, description, reference_id, reference_type, payment_method, status) VALUES
(1, 'TXN001', 1199.99, 'MARKETPLACE_PURCHASE', 'iPhone 15 Pro Max purchase', '1', 'PRODUCT', 'CARD', 'COMPLETED'),
(2, 'TXN002', 89.99, 'MARKETPLACE_PURCHASE', 'Summer Dress purchase', '4', 'PRODUCT', 'WALLET', 'COMPLETED'),
(3, 'TXN003', 25.50, 'RIDE_PAYMENT', 'Uber ride to downtown', '1', 'RIDE_REQUEST', 'CASH', 'COMPLETED'),
(1, 'TXN004', 45.67, 'FOOD_ORDER', 'Pizza Palace order', '1', 'FOOD_ORDER', 'CARD', 'COMPLETED'),
(2, 'TXN005', 150.00, 'WALLET_TOPUP', 'Wallet top-up', NULL, NULL, 'CARD', 'COMPLETED')
ON CONFLICT DO NOTHING;

-- Insert sample ride requests
INSERT INTO ride_requests (user_id, driver_id, pickup_location, dropoff_location, pickup_latitude, pickup_longitude, dropoff_latitude, dropoff_longitude, ride_type, total_fare, status, requested_at, completed_at) VALUES
(1, 3, 'Times Square, NYC', 'JFK Airport, NYC', 40.7580, -73.9855, 40.6413, -73.7781, 'SEDAN', 45.50, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '45 minutes'),
(2, 3, 'Hollywood Blvd, LA', 'LAX Airport, LA', 34.1022, -118.3268, 33.9425, -118.4081, 'ECONOMY', 28.75, 'COMPLETED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '35 minutes'),
(4, NULL, 'Miami Beach, FL', 'Downtown Miami, FL', 25.7907, -80.1300, 25.7617, -80.1918, 'LUXURY', 35.00, 'PENDING', CURRENT_TIMESTAMP - INTERVAL '30 minutes', NULL)
ON CONFLICT DO NOTHING;

-- Insert sample food orders
INSERT INTO food_orders (user_id, restaurant_id, order_number, subtotal, delivery_fee, total_amount, delivery_address, status, ordered_at, delivered_at) VALUES
(1, 1, 'ORD001', 25.98, 3.99, 29.97, '123 Main St, New York, NY', 'DELIVERED', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '25 minutes'),
(2, 2, 'ORD002', 18.98, 2.99, 21.97, '456 Oak Ave, Los Angeles, CA', 'DELIVERED', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '20 minutes'),
(3, 3, 'ORD003', 15.98, 4.99, 20.97, '789 Pine St, Chicago, IL', 'OUT_FOR_DELIVERY', CURRENT_TIMESTAMP - INTERVAL '15 minutes', NULL)
ON CONFLICT DO NOTHING;

-- Insert sample properties
INSERT INTO properties (owner_id, title, description, property_type, listing_type, price, area, bedrooms, bathrooms, address, latitude, longitude, city, amenities, features) VALUES
(4, 'Luxury Condo in Miami Beach', 'Beautiful 3BR/2BA condo with ocean view', 'CONDO', 'SALE', 750000.00, 1200.00, 3, 2, '123 Ocean Drive, Miami Beach, FL', 25.7907, -80.1300, 'Miami', ARRAY['Pool', 'Gym', 'Parking', 'Balcony'], ARRAY['Ocean View', 'Modern Kitchen', 'Hardwood Floors']),
(4, 'Downtown Apartment', 'Modern 2BR/1BA apartment in downtown', 'APARTMENT', 'RENT', 2500.00, 900.00, 2, 1, '456 Main St, Miami, FL', 25.7617, -80.1918, 'Miami', ARRAY['Gym', 'Parking', 'Rooftop'], ARRAY['City View', 'Modern Appliances', 'Walk-in Closet']),
(1, 'Family House in NYC', 'Spacious 4BR/3BA house with garden', 'HOUSE', 'SALE', 1200000.00, 2500.00, 4, 3, '789 Park Ave, New York, NY', 40.7128, -74.0060, 'New York', ARRAY['Garden', 'Garage', 'Basement'], ARRAY['Fireplace', 'Updated Kitchen', 'Master Suite'])
ON CONFLICT DO NOTHING;

-- Insert sample stickers
INSERT INTO sticker_packs (name, description, pack_type, price, is_free, download_count) VALUES
('Emoji Pack', 'Basic emoji stickers', 'EMOJI', 0.00, true, 1500),
('Animal Pack', 'Cute animal stickers', 'ANIMATED', 2.99, false, 890),
('Celebration Pack', 'Party and celebration stickers', 'STATIC', 1.99, false, 567),
('Food Pack', 'Delicious food stickers', 'ANIMATED', 2.99, false, 445)
ON CONFLICT DO NOTHING;

-- Insert sample coupons
INSERT INTO coupons (code, name, description, discount_type, discount_value, minimum_order_amount, usage_limit, valid_from, valid_until) VALUES
('WELCOME20', 'Welcome Discount', '20% off for new users', 'PERCENTAGE', 20.00, 50.00, 1000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 days'),
('FREESHIP', 'Free Shipping', 'Free shipping on orders above $25', 'FREE_SHIPPING', 0.00, 25.00, 500, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '15 days'),
('SAVE50', 'Save $50', '$50 off on orders above $200', 'FIXED_AMOUNT', 50.00, 200.00, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '7 days')
ON CONFLICT DO NOTHING;

-- Insert sample FAQs
INSERT INTO faqs (category, question, answer, order_index) VALUES
('Payments', 'How does escrow work?', 'Escrow protects both buyers and sellers by holding funds securely until the transaction is completed. The money is released only when both parties are satisfied.', 1),
('Payments', 'What is the refund process?', 'Refunds are processed within 3-5 business days after approval. You will receive a confirmation email once the refund is initiated.', 2),
('Payments', 'Are there any fees?', 'We charge a small fee for secure transactions to maintain our platform and ensure safety for all users.', 3),
('Account', 'How do I verify my account?', 'You can verify your account by providing a valid phone number and email address. Additional verification may be required for certain features.', 1),
('Account', 'How do I change my password?', 'Go to Settings > Account > Security > Change Password. You will need to enter your current password and create a new one.', 2),
('Technical', 'How do I report a bug?', 'You can report bugs through the Support section in the app or contact our technical team directly.', 1),
('Technical', 'Is the app available offline?', 'Some features work offline, but most functionality requires an internet connection.', 2)
ON CONFLICT DO NOTHING;

-- Insert sample notifications
INSERT INTO notifications (user_id, title, message, type, reference_id, reference_type, is_read) VALUES
(1, 'New Message', 'You have a new message from Jane Smith', 'MESSAGE', 1, 'CHAT', false),
(2, 'Post Liked', 'John Doe liked your post about sunset photography', 'POST_LIKE', 1, 'POST', false),
(3, 'Ride Request', 'New ride request to downtown area', 'RIDE_UPDATE', 3, 'RIDE_REQUEST', false),
(4, 'Payment Received', 'Payment of $750,000 received for property sale', 'PAYMENT', 1, 'TRANSACTION', false),
(5, 'Order Update', 'Your food order is out for delivery', 'ORDER_UPDATE', 3, 'FOOD_ORDER', false)
ON CONFLICT DO NOTHING;

-- Grant permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
