package com.bijli.live.data.models

import java.sql.Timestamp

// User related models
data class User(
    val id: Long = 0,
    val username: String,
    val email: String,
    val phone: String,
    val profilePhoto: String? = null,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val isActive: Boolean = true
)

data class Contact(
    val id: Long = 0,
    val userId: Long,
    val contactUserId: Long,
    val name: String,
    val phone: String,
    val isBlocked: Boolean = false,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis())
)

// Chat related models
data class Chat(
    val id: Long = 0,
    val name: String,
    val type: ChatType = ChatType.PRIVATE,
    val createdBy: Long,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val lastMessageAt: Timestamp? = null
)

data class ChatParticipant(
    val id: Long = 0,
    val chatId: Long,
    val userId: Long,
    val joinedAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val isAdmin: Boolean = false
)

data class Message(
    val id: Long = 0,
    val chatId: Long,
    val senderId: Long,
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val mediaUrl: String? = null,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val isDeleted: Boolean = false
)

enum class ChatType {
    PRIVATE, GROUP, BROADCAST
}

enum class MessageType {
    TEXT, IMAGE, VIDEO, AUDIO, DOCUMENT
}

// Marketplace models
data class Product(
    val id: Long = 0,
    val sellerId: Long,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val subcategory: String,
    val images: List<String> = emptyList(),
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isActive: Boolean = true,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis())
)

data class ProductImage(
    val id: Long = 0,
    val productId: Long,
    val imageUrl: String,
    val order: Int = 0
)

// Services models
data class RideRequest(
    val id: Long = 0,
    val userId: Long,
    val driverId: Long? = null,
    val pickupLocation: String,
    val dropoffLocation: String,
    val pickupLatitude: Double,
    val pickupLongitude: Double,
    val dropoffLatitude: Double,
    val dropoffLongitude: Double,
    val rideType: RideType = RideType.ECONOMY,
    val fare: Double,
    val status: RideStatus = RideStatus.PENDING,
    val requestedAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val completedAt: Timestamp? = null
)

data class FoodOrder(
    val id: Long = 0,
    val userId: Long,
    val restaurantId: Long,
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double,
    val deliveryAddress: String,
    val status: OrderStatus = OrderStatus.PENDING,
    val notes: String? = null,
    val orderedAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val deliveredAt: Timestamp? = null
)

data class OrderItem(
    val id: Long = 0,
    val orderId: Long,
    val itemName: String,
    val quantity: Int,
    val price: Double,
    val notes: String? = null
)

enum class RideType {
    ECONOMY, SEDAN, LUXURY
}

enum class RideStatus {
    PENDING, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED
}

enum class OrderStatus {
    PENDING, CONFIRMED, PREPARING, READY, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
}

// Transaction models
data class Transaction(
    val id: Long = 0,
    val userId: Long,
    val amount: Double,
    val type: TransactionType,
    val description: String,
    val referenceId: String? = null,
    val status: TransactionStatus = TransactionStatus.PENDING,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val completedAt: Timestamp? = null
)

enum class TransactionType {
    PAYMENT, REFUND, RIDE_PAYMENT, FOOD_ORDER, MARKETPLACE_PURCHASE
}

enum class TransactionStatus {
    PENDING, COMPLETED, FAILED, CANCELLED
}

// Post and Social models
data class Post(
    val id: Long = 0,
    val userId: Long,
    val content: String,
    val mediaUrls: List<String> = emptyList(),
    val postType: PostType = PostType.TEXT,
    val isPublic: Boolean = true,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis())
)

data class PostLike(
    val id: Long = 0,
    val postId: Long,
    val userId: Long,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis())
)

data class PostComment(
    val id: Long = 0,
    val postId: Long,
    val userId: Long,
    val content: String,
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis())
)

enum class PostType {
    TEXT, IMAGE, VIDEO, MOMENT
}
