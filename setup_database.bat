@echo off
REM Bijli Live Database Setup Script for Windows
REM This script sets up the PostgreSQL database for the Bijli Live app

echo 🚀 Bijli Live Database Setup
echo ============================

REM Database configuration
set DB_NAME=bijli_live
set DB_USER=postgres
set DB_PASSWORD=Karachi5846$
set DB_HOST=localhost
set DB_PORT=5432

echo 📊 Database Configuration:
echo   Host: %DB_HOST%
echo   Port: %DB_PORT%
echo   Database: %DB_NAME%
echo   User: %DB_USER%
echo.

REM Check if PostgreSQL is running
echo 🔍 Checking PostgreSQL service...
pg_isready -h %DB_HOST% -p %DB_PORT% -U %DB_USER% >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ PostgreSQL is not running or not accessible
    echo    Please start PostgreSQL service and try again
    pause
    exit /b 1
)

echo ✅ PostgreSQL is running

REM Create database if it doesn't exist
echo 📁 Creating database if it doesn't exist...
set PGPASSWORD=%DB_PASSWORD%
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -c "CREATE DATABASE %DB_NAME%;" >nul 2>&1
if %errorlevel% neq 0 (
    echo Database already exists or creation failed
)

REM Run schema
echo 📋 Running database schema...
if exist "database_schema.sql" (
    psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -f database_schema.sql
    if %errorlevel% equ 0 (
        echo ✅ Database schema created successfully
    ) else (
        echo ❌ Failed to create database schema
        pause
        exit /b 1
    )
) else (
    echo ❌ database_schema.sql file not found
    pause
    exit /b 1
)

REM Test database connection
echo 🧪 Testing database connection...
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -c "SELECT COUNT(*) FROM users;" >nul 2>&1
if %errorlevel% equ 0 (
    echo ✅ Database connection test successful
) else (
    echo ❌ Database connection test failed
    pause
    exit /b 1
)

REM Show sample data
echo 📊 Sample data in database:
echo Users:
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -c "SELECT id, username, email FROM users LIMIT 3;"

echo.
echo Products:
psql -h %DB_HOST% -p %DB_PORT% -U %DB_USER% -d %DB_NAME% -c "SELECT id, title, price, category FROM products LIMIT 3;"

echo.
echo 🎉 Database setup completed successfully!
echo.
echo 📱 Next steps:
echo    1. Update DatabaseConfig.kt with your database details
echo    2. Build and run the Android app
echo    3. Test the app functionality
echo.
echo 🔧 Database connection details for Android app:
echo    Host: %DB_HOST%
echo    Port: %DB_PORT%
echo    Database: %DB_NAME%
echo    Username: %DB_USER%
echo    Password: %DB_PASSWORD%

pause
