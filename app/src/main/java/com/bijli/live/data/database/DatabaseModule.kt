package com.bijli.live.data.database

import android.content.Context
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseConnection(@ApplicationContext context: Context): DatabaseConnection {
        return DatabaseConnection(context)
    }
}

class DatabaseConnection(private val context: Context) {
    
    private val databaseConfig = DatabaseConfig()
    
    suspend fun getConnection(): Connection = withContext(Dispatchers.IO) {
        try {
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(
                databaseConfig.url,
                databaseConfig.username,
                databaseConfig.password
            )
        } catch (e: Exception) {
            throw DatabaseException("Failed to connect to database: ${e.message}", e)
        }
    }
    
    fun getHikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = databaseConfig.url
            username = databaseConfig.username
            password = databaseConfig.password
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 10
            minimumIdle = 2
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }
        return HikariDataSource(config)
    }
}

data class DatabaseConfig(
    val host: String = "localhost",
    val port: Int = 5432,
    val database: String = "bijli_live",
    val username: String = "postgres",
    val password: String = "Karachi5846$"
) {
    val url: String
        get() = "jdbc:postgresql://$host:$port/$database?sslmode=disable"
}

class DatabaseException(message: String, cause: Throwable? = null) : Exception(message, cause)
