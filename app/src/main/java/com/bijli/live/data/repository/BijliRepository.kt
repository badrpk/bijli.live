package com.bijli.live.data.repository

import com.bijli.live.data.database.DatabaseConnection
import com.bijli.live.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.PreparedStatement
import java.sql.ResultSet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BijliRepository @Inject constructor(
    private val databaseConnection: DatabaseConnection
) {
    
    // User operations
    suspend fun createUser(user: User): Long = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            INSERT INTO users (username, email, phone, profile_photo, created_at, updated_at, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setString(1, user.username)
            stmt.setString(2, user.email)
            stmt.setString(3, user.phone)
            stmt.setString(4, user.profilePhoto)
            stmt.setTimestamp(5, user.createdAt)
            stmt.setTimestamp(6, user.updatedAt)
            stmt.setBoolean(7, user.isActive)
            
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getLong("id") else throw Exception("Failed to create user")
        }
    }
    
    suspend fun getUserById(id: Long): User? = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = "SELECT * FROM users WHERE id = ? AND is_active = true"
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, id)
            val rs = stmt.executeQuery()
            if (rs.next()) mapResultSetToUser(rs) else null
        }
    }
    
    suspend fun getUserByEmail(email: String): User? = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = "SELECT * FROM users WHERE email = ? AND is_active = true"
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setString(1, email)
            val rs = stmt.executeQuery()
            if (rs.next()) mapResultSetToUser(rs) else null
        }
    }
    
    // Chat operations
    suspend fun createChat(chat: Chat): Long = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            INSERT INTO chats (name, type, created_by, created_at)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setString(1, chat.name)
            stmt.setString(2, chat.type.name)
            stmt.setLong(3, chat.createdBy)
            stmt.setTimestamp(4, chat.createdAt)
            
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getLong("id") else throw Exception("Failed to create chat")
        }
    }
    
    suspend fun getChatsByUserId(userId: Long): List<Chat> = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            SELECT c.* FROM chats c
            JOIN chat_participants cp ON c.id = cp.chat_id
            WHERE cp.user_id = ?
            ORDER BY c.last_message_at DESC NULLS LAST, c.created_at DESC
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, userId)
            val rs = stmt.executeQuery()
            val chats = mutableListOf<Chat>()
            while (rs.next()) {
                chats.add(mapResultSetToChat(rs))
            }
            chats
        }
    }
    
    suspend fun sendMessage(message: Message): Long = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            INSERT INTO messages (chat_id, sender_id, content, message_type, media_url, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, message.chatId)
            stmt.setLong(2, message.senderId)
            stmt.setString(3, message.content)
            stmt.setString(4, message.messageType.name)
            stmt.setString(5, message.mediaUrl)
            stmt.setTimestamp(6, message.createdAt)
            
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getLong("id") else throw Exception("Failed to send message")
        }
    }
    
    suspend fun getMessagesByChatId(chatId: Long, limit: Int = 50): List<Message> = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            SELECT * FROM messages 
            WHERE chat_id = ? AND is_deleted = false
            ORDER BY created_at DESC
            LIMIT ?
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, chatId)
            stmt.setInt(2, limit)
            val rs = stmt.executeQuery()
            val messages = mutableListOf<Message>()
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs))
            }
            messages.reversed() // Return in chronological order
        }
    }
    
    // Product operations
    suspend fun createProduct(product: Product): Long = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            INSERT INTO products (seller_id, title, description, price, category, subcategory, 
                                location, latitude, longitude, is_active, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, product.sellerId)
            stmt.setString(2, product.title)
            stmt.setString(3, product.description)
            stmt.setDouble(4, product.price)
            stmt.setString(5, product.category)
            stmt.setString(6, product.subcategory)
            stmt.setString(7, product.location)
            stmt.setDouble(8, product.latitude ?: 0.0)
            stmt.setDouble(9, product.longitude ?: 0.0)
            stmt.setBoolean(10, product.isActive)
            stmt.setTimestamp(11, product.createdAt)
            stmt.setTimestamp(12, product.updatedAt)
            
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getLong("id") else throw Exception("Failed to create product")
        }
    }
    
    suspend fun getProductsByCategory(category: String, subcategory: String? = null): List<Product> = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = if (subcategory != null) {
            "SELECT * FROM products WHERE category = ? AND subcategory = ? AND is_active = true ORDER BY created_at DESC"
        } else {
            "SELECT * FROM products WHERE category = ? AND is_active = true ORDER BY created_at DESC"
        }
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setString(1, category)
            if (subcategory != null) {
                stmt.setString(2, subcategory)
            }
            val rs = stmt.executeQuery()
            val products = mutableListOf<Product>()
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs))
            }
            products
        }
    }
    
    // Transaction operations
    suspend fun createTransaction(transaction: Transaction): Long = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            INSERT INTO transactions (user_id, amount, type, description, reference_id, status, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, transaction.userId)
            stmt.setDouble(2, transaction.amount)
            stmt.setString(3, transaction.type.name)
            stmt.setString(4, transaction.description)
            stmt.setString(5, transaction.referenceId)
            stmt.setString(6, transaction.status.name)
            stmt.setTimestamp(7, transaction.createdAt)
            
            val rs = stmt.executeQuery()
            if (rs.next()) rs.getLong("id") else throw Exception("Failed to create transaction")
        }
    }
    
    suspend fun getTransactionsByUserId(userId: Long, limit: Int = 100): List<Transaction> = withContext(Dispatchers.IO) {
        val connection = databaseConnection.getConnection()
        val sql = """
            SELECT * FROM transactions 
            WHERE user_id = ?
            ORDER BY created_at DESC
            LIMIT ?
        """.trimIndent()
        
        connection.prepareStatement(sql).use { stmt ->
            stmt.setLong(1, userId)
            stmt.setInt(2, limit)
            val rs = stmt.executeQuery()
            val transactions = mutableListOf<Transaction>()
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs))
            }
            transactions
        }
    }
    
    // Helper methods to map ResultSet to data classes
    private fun mapResultSetToUser(rs: ResultSet): User {
        return User(
            id = rs.getLong("id"),
            username = rs.getString("username"),
            email = rs.getString("email"),
            phone = rs.getString("phone"),
            profilePhoto = rs.getString("profile_photo"),
            createdAt = rs.getTimestamp("created_at"),
            updatedAt = rs.getTimestamp("updated_at"),
            isActive = rs.getBoolean("is_active")
        )
    }
    
    private fun mapResultSetToChat(rs: ResultSet): Chat {
        return Chat(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            type = ChatType.valueOf(rs.getString("type")),
            createdBy = rs.getLong("created_by"),
            createdAt = rs.getTimestamp("created_at"),
            lastMessageAt = rs.getTimestamp("last_message_at")
        )
    }
    
    private fun mapResultSetToMessage(rs: ResultSet): Message {
        return Message(
            id = rs.getLong("id"),
            chatId = rs.getLong("chat_id"),
            senderId = rs.getLong("sender_id"),
            content = rs.getString("content"),
            messageType = MessageType.valueOf(rs.getString("message_type")),
            mediaUrl = rs.getString("media_url"),
            createdAt = rs.getTimestamp("created_at"),
            isDeleted = rs.getBoolean("is_deleted")
        )
    }
    
    private fun mapResultSetToProduct(rs: ResultSet): Product {
        return Product(
            id = rs.getLong("id"),
            sellerId = rs.getLong("seller_id"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            price = rs.getDouble("price"),
            category = rs.getString("category"),
            subcategory = rs.getString("subcategory"),
            location = rs.getString("location"),
            latitude = rs.getDouble("latitude").takeIf { rs.wasNull().not() },
            longitude = rs.getDouble("longitude").takeIf { rs.wasNull().not() },
            isActive = rs.getBoolean("is_active"),
            createdAt = rs.getTimestamp("created_at"),
            updatedAt = rs.getTimestamp("updated_at")
        )
    }
    
    private fun mapResultSetToTransaction(rs: ResultSet): Transaction {
        return Transaction(
            id = rs.getLong("id"),
            userId = rs.getLong("user_id"),
            amount = rs.getDouble("amount"),
            type = TransactionType.valueOf(rs.getString("type")),
            description = rs.getString("description"),
            referenceId = rs.getString("reference_id"),
            status = TransactionStatus.valueOf(rs.getString("status")),
            createdAt = rs.getTimestamp("created_at"),
            completedAt = rs.getTimestamp("completed_at")
        )
    }
}
