package com.bijli.live.ui.screens.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bijli.live.ui.components.BottomNavigationBar

@Composable
fun MeScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Account", "Pay & Services", "Favourites", "My Posts", "Cards & Offers", "Stickers", "Settings", "Support")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "👤 Me",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> AccountContent()
            1 -> PayServicesContent()
            2 -> FavouritesContent()
            3 -> MyPostsContent()
            4 -> CardsOffersContent()
            5 -> StickersContent()
            6 -> SettingsContent()
            7 -> SupportContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun AccountContent() {
    var showPhotoDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("None") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Account → Edit Profile → Change Photo → Upload from Gallery",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Profile Photo Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column {
                            Text(
                                text = "John Doe",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "john.doe@email.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Member since 2023",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        IconButton(
                            onClick = { showPhotoDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile"
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Photo Edit Options
                    Text(
                        text = "Photo Edit Options:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showPhotoDialog = true }
                        ) {
                            Text("Upload from Gallery")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Crop image */ }
                        ) {
                            Text("Crop Image")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Rotate image */ }
                        ) {
                            Text("Rotate")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Filter Options
                    Text(
                        text = "Apply Filter:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val filters = listOf("None", "Vintage", "Black & White", "Sepia", "Bright")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        filters.forEach { filter ->
                            FilterChip(
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) },
                                selected = selectedFilter == filter
                            )
                        }
                    }
                }
            }
        }
    }

    // Photo Dialog
    if (showPhotoDialog) {
        AlertDialog(
            onDismissRequest = { showPhotoDialog = false },
            title = { Text("Change Profile Photo") },
            text = {
                Column {
                    Text("Select photo source:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val sources = listOf("Camera", "Gallery", "Remove Photo")
                    sources.forEach { source ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Handle source selection */ }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (source) {
                                    "Camera" -> Icons.Default.CameraAlt
                                    "Gallery" -> Icons.Default.PhotoLibrary
                                    "Remove Photo" -> Icons.Default.Delete
                                    else -> Icons.Default.Image
                                },
                                contentDescription = source,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(source)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPhotoDialog = false }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPhotoDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PayServicesContent() {
    var selectedDateFilter by remember { mutableStateOf("Today") }
    var showExportDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Pay & Services → Transaction History → Export CSV → Date Filter",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Date Filter
                    Text(
                        text = "Date Filter:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val dateFilters = listOf("Today", "This week", "This month", "Custom range")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        dateFilters.forEach { filter ->
                            FilterChip(
                                onClick = { selectedDateFilter = filter },
                                label = { Text(filter) },
                                selected = selectedDateFilter == filter
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Transaction History
                    Text(
                        text = "Transaction History:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val transactions = listOf(
                        TransactionInfo("Payment to John", "₹500", "2 hours ago", "Completed"),
                        TransactionInfo("Ride Payment", "₹200", "1 day ago", "Completed"),
                        TransactionInfo("Food Order", "₹350", "2 days ago", "Completed"),
                        TransactionInfo("Refund", "₹150", "3 days ago", "Processed")
                    )
                    
                    transactions.forEach { transaction ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payment,
                                    contentDescription = "Transaction",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = transaction.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = transaction.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = transaction.status,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Text(
                                    text = transaction.amount,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showExportDialog = true }
                        ) {
                            Text("Export CSV")
                        }
                        
                        OutlinedButton(
                            onClick = { /* View details */ }
                        ) {
                            Text("View Details")
                        }
                    }
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Transaction History") },
            text = {
                Column {
                    Text("Date range: $selectedDateFilter")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total transactions: 4")
                    Text("Total amount: ₹1,200")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Export format: CSV")
                    Text("File size: ~2KB")
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Export")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FavouritesContent() {
    var showShareDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Favourites → Open Post → Full Screen View → Share",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Favourite Posts
                    Text(
                        text = "Favourite Posts:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val favouritePosts = listOf(
                        FavouritePost("Amazing sunset photo", "2 days ago", "1.2K likes"),
                        FavouritePost("Tech review video", "1 week ago", "856 likes"),
                        FavouritePost("Travel blog post", "2 weeks ago", "2.1K likes"),
                        FavouritePost("Recipe tutorial", "3 weeks ago", "445 likes")
                    )
                    
                    favouritePosts.forEach { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedPost = post.title
                                    showShareDialog = true 
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = "Post",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = post.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = post.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = post.likes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                IconButton(
                                    onClick = { 
                                        selectedPost = post.title
                                        showShareDialog = true 
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share"
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { /* Full screen view */ }
                        ) {
                            Text("Full Screen View")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Copy link */ }
                        ) {
                            Text("Copy Link")
                        }
                    }
                }
            }
        }
    }

    // Share Dialog
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share Post") },
            text = {
                Column {
                    Text("Post: $selectedPost")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Share options:")
                    Text("• Copy link")
                    Text("• Share to WhatsApp")
                    Text("• Share to Facebook")
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MyPostsContent() {
    var showStatsDialog by remember { mutableStateOf(false) }
    var dailyLikes by remember { mutableStateOf(150) }
    var previousDayLikes by remember { mutableStateOf(120) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "My Posts → View Stats → Likes Count → Daily Likes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Stats Overview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard("Total Posts", "24", "This month")
                        StatCard("Total Likes", "2.5K", "+15% from last month")
                        StatCard("Followers", "1.2K", "+8% from last month")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Daily Likes Chart
                    Text(
                        text = "Daily Likes Chart:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Today: $dailyLikes likes")
                                Text("Yesterday: $previousDayLikes likes")
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Simple chart representation
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                repeat(7) { index ->
                                    Box(
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height((50 + index * 10).dp)
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Last 7 days",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showStatsDialog = true }
                        ) {
                            Text("View Detailed Stats")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Export numbers */ }
                        ) {
                            Text("Export Numbers")
                        }
                    }
                }
            }
        }
    }

    // Stats Dialog
    if (showStatsDialog) {
        AlertDialog(
            onDismissRequest = { showStatsDialog = false },
            title = { Text("Detailed Stats") },
            text = {
                Column {
                    Text("Likes Analysis:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Today: $dailyLikes likes")
                    Text("• Yesterday: $previousDayLikes likes")
                    Text("• Change: +${dailyLikes - previousDayLikes} likes")
                    Text("• Growth: +${((dailyLikes - previousDayLikes) * 100 / previousDayLikes)}%")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Chart view available")
                    Text("Compare with previous day")
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatsDialog = false }) {
                    Text("Chart View")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStatsDialog = false }) {
                    Text("Export")
                }
            }
        )
    }
}

@Composable
fun CardsOffersContent() {
    var showCouponDialog by remember { mutableStateOf(false) }
    var appliedCoupon by remember { mutableStateOf("") }
    var couponCode by remember { mutableStateOf("SAVE20") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Cards & Offers → View Coupon Code → Apply in Cart → Confirm",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Available Coupons
                    Text(
                        text = "Available Coupons:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val coupons = listOf(
                        CouponInfo("SAVE20", "20% off on all items", "Valid till Dec 31"),
                        CouponInfo("FREESHIP", "Free shipping on orders above ₹500", "Valid till Jan 15"),
                        CouponInfo("NEWUSER", "50% off for new users", "Valid till Feb 28")
                    )
                    
                    coupons.forEach { coupon ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = "Coupon",
                                    modifier = Modifier.size(24.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = coupon.code,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = coupon.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = coupon.validity,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Button(
                                    onClick = { 
                                        couponCode = coupon.code
                                        showCouponDialog = true 
                                    }
                                ) {
                                    Text("Apply")
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Cart Summary
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Cart Summary:",
                                style = MaterialTheme.typography.titleSmall
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtotal:")
                                Text("₹1,000")
                            }
                            
                            if (appliedCoupon.isNotEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Discount ($appliedCoupon):")
                                    Text("-₹200", color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", fontWeight = FontWeight.Bold)
                                Text("₹${if (appliedCoupon.isNotEmpty()) "800" else "1,000"}", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { 
                                appliedCoupon = couponCode
                                showCouponDialog = true 
                            }
                        ) {
                            Text("Apply Coupon")
                        }
                        
                        OutlinedButton(
                            onClick = { appliedCoupon = "" }
                        ) {
                            Text("Undo Coupon")
                        }
                    }
                    
                    if (appliedCoupon.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Applied",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Coupon $appliedCoupon applied successfully",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Coupon Dialog
    if (showCouponDialog) {
        AlertDialog(
            onDismissRequest = { showCouponDialog = false },
            title = { Text("Apply Coupon") },
            text = {
                Column {
                    Text("Coupon Code: $couponCode")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Discount: 20% off")
                    Text("Savings: ₹200")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Apply this coupon to your cart?")
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    appliedCoupon = couponCode
                    showCouponDialog = false 
                }) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCouponDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StickersContent() {
    var showStickerDialog by remember { mutableStateOf(false) }
    var selectedPack by remember { mutableStateOf("Emoji Pack") }
    var isAutoPlay by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Sticker Gallery → Browse Packs → Preview Pack → Auto-play Animation",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sticker Packs
                    Text(
                        text = "Sticker Packs:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val stickerPacks = listOf(
                        StickerPack("Emoji Pack", "50 stickers", "Free"),
                        StickerPack("Animal Pack", "30 stickers", "₹29"),
                        StickerPack("Celebration Pack", "25 stickers", "₹19"),
                        StickerPack("Food Pack", "40 stickers", "₹39")
                    )
                    
                    stickerPacks.forEach { pack ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedPack = pack.name
                                    showStickerDialog = true 
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEmotions,
                                        contentDescription = "Stickers",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = pack.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = pack.count,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Text(
                                    text = pack.price,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Animation Controls
                    Text(
                        text = "Animation Controls:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { isAutoPlay = !isAutoPlay }
                        ) {
                            Text(if (isAutoPlay) "Auto-play ON" else "Auto-play OFF")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Loop preview */ }
                        ) {
                            Text("Loop Preview")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Play once */ }
                        ) {
                            Text("Play Once")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Mute animation */ }
                        ) {
                            Text("Mute Animation")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Download pack */ }
                        ) {
                            Text("Download Pack")
                        }
                    }
                }
            }
        }
    }

    // Sticker Dialog
    if (showStickerDialog) {
        AlertDialog(
            onDismissRequest = { showStickerDialog = false },
            title = { Text("Sticker Pack Preview") },
            text = {
                Column {
                    Text("Pack: $selectedPack")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Sticker preview grid
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(6) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .clip(RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("😀", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Animation: ${if (isAutoPlay) "Auto-play" else "Manual"}")
                    Text("Loop: Enabled")
                    Text("Sound: Muted")
                }
            },
            confirmButton = {
                TextButton(onClick = { showStickerDialog = false }) {
                    Text("Download")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStickerDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun SettingsContent() {
    var showBlockDialog by remember { mutableStateOf(false) }
    var blockedContacts by remember { mutableStateOf(setOf<String>()) }
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Settings → Privacy → Block Contacts → Add Blocked Contact",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Privacy Settings
                    Text(
                        text = "Privacy Settings:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val privacyOptions = listOf(
                        PrivacyOption("Block Contacts", "Manage blocked users"),
                        PrivacyOption("Profile Visibility", "Control who can see your profile"),
                        PrivacyOption("Last Seen", "Control when others can see you"),
                        PrivacyOption("Read Receipts", "Control read receipts")
                    )
                    
                    privacyOptions.forEach { option ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Privacy",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = option.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { 
                                        if (option.title == "Block Contacts") {
                                            showBlockDialog = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Open"
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Blocked Contacts
                    if (blockedContacts.isNotEmpty()) {
                        Text(
                            text = "Blocked Contacts:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        blockedContacts.forEach { contact ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonOff,
                                        contentDescription = "Blocked",
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Text(
                                        text = contact,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Spacer(modifier = Modifier.weight(1f))
                                    
                                    IconButton(
                                        onClick = { 
                                            blockedContacts = blockedContacts - contact
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Undo,
                                            contentDescription = "Unblock"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Block Dialog
    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false },
            title = { Text("Block Contact") },
            text = {
                Column {
                    Text("Search contact to block:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Contact name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (searchQuery.isNotEmpty()) {
                        Text("Search results:")
                        Text("• $searchQuery")
                        Text("• ${searchQuery}2")
                        Text("• ${searchQuery}3")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    if (searchQuery.isNotEmpty()) {
                        blockedContacts = blockedContacts + searchQuery
                        searchQuery = ""
                    }
                    showBlockDialog = false 
                }) {
                    Text("Block")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SupportContent() {
    var showPINDialog by remember { mutableStateOf(false) }
    var pinMessage by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Support → PIN Request → Send Request → Auto-message Template",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Support Options
                    Text(
                        text = "Support Options:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val supportOptions = listOf(
                        SupportOption("PIN Request", "Request account PIN"),
                        SupportOption("Live Chat", "Chat with support team"),
                        SupportOption("Email Support", "Send email to support"),
                        SupportOption("FAQ", "Browse frequently asked questions")
                    )
                    
                    supportOptions.forEach { option ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Help,
                                    contentDescription = "Support",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = option.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { 
                                        if (option.title == "PIN Request") {
                                            showPINDialog = true
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Open"
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quick Actions
                    Text(
                        text = "Quick Actions:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showPINDialog = true }
                        ) {
                            Text("Request PIN")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Live chat */ }
                        ) {
                            Text("Live Chat")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Email support */ }
                        ) {
                            Text("Email")
                        }
                    }
                }
            }
        }
    }

    // PIN Dialog
    if (showPINDialog) {
        AlertDialog(
            onDismissRequest = { showPINDialog = false },
            title = { Text("PIN Request") },
            text = {
                Column {
                    Text("Auto-message template:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = pinMessage,
                        onValueChange = { pinMessage = it },
                        label = { Text("Message") },
                        placeholder = { Text("I need help with my account PIN...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Pre-filled message:")
                    Text("Hi, I need assistance with my account PIN. Please help me reset it.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showPINDialog = false }) {
                    Text("Send Request")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPINDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String) {
    Card(
        modifier = Modifier.width(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

data class TransactionInfo(
    val description: String,
    val amount: String,
    val time: String,
    val status: String
)

data class FavouritePost(
    val title: String,
    val time: String,
    val likes: String
)

data class CouponInfo(
    val code: String,
    val description: String,
    val validity: String
)

data class StickerPack(
    val name: String,
    val count: String,
    val price: String
)

data class PrivacyOption(
    val title: String,
    val description: String
)

data class SupportOption(
    val title: String,
    val description: String
)
