#!/bin/bash

# Bijli Live Database Setup Script
# This script sets up the PostgreSQL database for the Bijli Live app

echo "🚀 Bijli Live Database Setup"
echo "============================"

# Database configuration
DB_NAME="bijli_live"
DB_USER="postgres"
DB_PASSWORD="Karachi5846$"
DB_HOST="localhost"
DB_PORT="5432"

echo "📊 Database Configuration:"
echo "  Host: $DB_HOST"
echo "  Port: $DB_PORT"
echo "  Database: $DB_NAME"
echo "  User: $DB_USER"
echo ""

# Check if PostgreSQL is running
echo "🔍 Checking PostgreSQL service..."
if ! pg_isready -h $DB_HOST -p $DB_PORT -U $DB_USER; then
    echo "❌ PostgreSQL is not running or not accessible"
    echo "   Please start PostgreSQL service and try again"
    exit 1
fi

echo "✅ PostgreSQL is running"

# Create database if it doesn't exist
echo "📁 Creating database if it doesn't exist..."
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -c "CREATE DATABASE $DB_NAME;" 2>/dev/null || echo "Database already exists"

# Run schema
echo "📋 Running database schema..."
if [ -f "database_schema.sql" ]; then
    PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f database_schema.sql
    if [ $? -eq 0 ]; then
        echo "✅ Database schema created successfully"
    else
        echo "❌ Failed to create database schema"
        exit 1
    fi
else
    echo "❌ database_schema.sql file not found"
    exit 1
fi

# Test database connection
echo "🧪 Testing database connection..."
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT COUNT(*) FROM users;" > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Database connection test successful"
else
    echo "❌ Database connection test failed"
    exit 1
fi

# Show sample data
echo "📊 Sample data in database:"
echo "Users:"
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT id, username, email FROM users LIMIT 3;"

echo ""
echo "Products:"
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT id, title, price, category FROM products LIMIT 3;"

echo ""
echo "🎉 Database setup completed successfully!"
echo ""
echo "📱 Next steps:"
echo "   1. Update DatabaseConfig.kt with your database details"
echo "   2. Build and run the Android app"
echo "   3. Test the app functionality"
echo ""
echo "🔧 Database connection details for Android app:"
echo "   Host: $DB_HOST"
echo "   Port: $DB_PORT"
echo "   Database: $DB_NAME"
echo "   Username: $DB_USER"
echo "   Password: $DB_PASSWORD"
