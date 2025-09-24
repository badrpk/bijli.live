package com.bijli.live.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun ServicesScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Marketplace", "Food Delivery", "Ride Hailing", "Real Estate")

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
                    text = "🧩 Services",
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
            0 -> MarketplaceContent()
            1 -> FoodDeliveryContent()
            2 -> RideHailingContent()
            3 -> RealEstateContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun MarketplaceContent() {
    var selectedCategory by remember { mutableStateOf("Electronics") }
    var selectedSubCategory by remember { mutableStateOf("Mobiles") }
    var showUploadDialog by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf(listOf<String>()) }

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
                        text = "Marketplace → New → Upload Images → Multi-select",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Upload Images Section
                    Text(
                        text = "Upload Images:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showUploadDialog = true }
                        ) {
                            Text("Pick Multiple Files")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Drag reorder */ }
                        ) {
                            Text("Drag Reorder")
                        }
                        
                        OutlinedButton(
                            onClick = { selectedImages = emptyList() }
                        ) {
                            Text("Delete All")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Image Preview Grid
                    if (selectedImages.isNotEmpty()) {
                        Text(
                            text = "Selected Images:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LazyColumn(
                            modifier = Modifier.height(200.dp)
                        ) {
                            items(selectedImages) { image ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
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
                                                contentDescription = "Image",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.width(12.dp))
                                        
                                        Text(
                                            text = image,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        
                                        Spacer(modifier = Modifier.weight(1f))
                                        
                                        IconButton(
                                            onClick = { 
                                                selectedImages = selectedImages - image
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category Selection
                    Text(
                        text = "Category Filters:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val categories = listOf("Electronics", "Fashion", "Home", "Sports", "Books")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        categories.forEach { category ->
                            FilterChip(
                                onClick = { selectedCategory = category },
                                label = { Text(category) },
                                selected = selectedCategory == category
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Fashion Subcategories
                    if (selectedCategory == "Fashion") {
                        Text(
                            text = "Fashion Categories:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val fashionSubcategories = listOf("Men's wear", "Women's wear", "Kids' wear", "Accessories")
                        fashionSubcategories.forEach { subCategory ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSubCategory = subCategory },
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (subCategory) {
                                            "Men's wear" -> Icons.Default.Man
                                            "Women's wear" -> Icons.Default.Woman
                                            "Kids' wear" -> Icons.Default.ChildCare
                                            "Accessories" -> Icons.Default.Accessibility
                                            else -> Icons.Default.ShoppingBag
                                        },
                                        contentDescription = subCategory,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = subCategory,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "25 items",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Upload Dialog
    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Upload Images") },
            text = {
                Column {
                    Text("Select multiple images:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val sampleImages = listOf("image1.jpg", "image2.jpg", "image3.jpg", "image4.jpg")
                    sampleImages.forEach { image ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    if (image in selectedImages) {
                                        selectedImages = selectedImages - image
                                    } else {
                                        selectedImages = selectedImages + image
                                    }
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = image in selectedImages,
                                onCheckedChange = { 
                                    if (image in selectedImages) {
                                        selectedImages = selectedImages - image
                                    } else {
                                        selectedImages = selectedImages + image
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(image)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Upload")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FoodDeliveryContent() {
    var selectedRestaurant by remember { mutableStateOf("Pizza Palace") }
    var cartItems by remember { mutableStateOf(mapOf<String, Int>()) }
    var showCartDialog by remember { mutableStateOf(false) }

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
                        text = "Food Delivery → Browse → Add to Cart → Select Quantity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Restaurant Selection
                    Text(
                        text = "Restaurants:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val restaurants = listOf("Pizza Palace", "Burger King", "Sushi Master", "Taco Bell")
                    restaurants.forEach { restaurant ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRestaurant = restaurant },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = "Restaurant",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = restaurant,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "4.5 ⭐",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Menu Items
                    Text(
                        text = "Menu Items:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val menuItems = listOf(
                        MenuItem("Margherita Pizza", "₹299", "Extra spicy"),
                        MenuItem("Chicken Burger", "₹199", "No onions"),
                        MenuItem("Caesar Salad", "₹149", "Extra dressing"),
                        MenuItem("Chocolate Cake", "₹99", "Extra chocolate")
                    )
                    
                    menuItems.forEach { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = item.price,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Note: ${item.note}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { 
                                            val current = cartItems[item.name] ?: 0
                                            if (current > 0) {
                                                cartItems = cartItems + (item.name to (current - 1))
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = "Decrease"
                                        )
                                    }
                                    
                                    Text(
                                        text = "${cartItems[item.name] ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                    
                                    IconButton(
                                        onClick = { 
                                            val current = cartItems[item.name] ?: 0
                                            cartItems = cartItems + (item.name to (current + 1))
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Increase"
                                        )
                                    }
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
                            onClick = { showCartDialog = true }
                        ) {
                            Text("View Cart")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Save for later */ }
                        ) {
                            Text("Save for Later")
                        }
                    }
                }
            }
        }
    }

    // Cart Dialog
    if (showCartDialog) {
        AlertDialog(
            onDismissRequest = { showCartDialog = false },
            title = { Text("Shopping Cart") },
            text = {
                Column {
                    Text("Items in cart:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (cartItems.isEmpty()) {
                        Text("Cart is empty")
                    } else {
                        cartItems.forEach { (item, quantity) ->
                            if (quantity > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("$item x $quantity")
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text("₹${quantity * 299}")
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCartDialog = false }) {
                    Text("Checkout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCartDialog = false }) {
                    Text("Continue Shopping")
                }
            }
        )
    }
}

@Composable
fun RideHailingContent() {
    var selectedRideType by remember { mutableStateOf("Car") }
    var showDriverDialog by remember { mutableStateOf(false) }
    var showEarningsDialog by remember { mutableStateOf(false) }

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
                        text = "Ride Hailing → Request → Ride Type → Car",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Ride Type Selection
                    Text(
                        text = "Ride Types:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val rideTypes = listOf("Economy", "Sedan", "Luxury")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rideTypes.forEach { type ->
                            FilterChip(
                                onClick = { selectedRideType = type },
                                label = { Text(type) },
                                selected = selectedRideType == type
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Ride Options
                    Text(
                        text = "Ride Options:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val rideOptions = listOf(
                        RideOption("Economy", "₹150", "Toyota Etios", "4.8 ⭐"),
                        RideOption("Sedan", "₹200", "Honda City", "4.9 ⭐"),
                        RideOption("Luxury", "₹300", "BMW 3 Series", "5.0 ⭐")
                    )
                    
                    rideOptions.forEach { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showDriverDialog = true },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = "Car",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = option.type,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = option.car,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = option.rating,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Text(
                                    text = option.price,
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
                            onClick = { showDriverDialog = true }
                        ) {
                            Text("Request Ride")
                        }
                        
                        OutlinedButton(
                            onClick = { showEarningsDialog = true }
                        ) {
                            Text("Driver Earnings")
                        }
                    }
                }
            }
        }
    }

    // Driver Dialog
    if (showDriverDialog) {
        AlertDialog(
            onDismissRequest = { showDriverDialog = false },
            title = { Text("Driver Details") },
            text = {
                Column {
                    Text("Driver: Rajesh Kumar")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Car: Honda City")
                    Text("Rating: 4.9 ⭐")
                    Text("ETA: 5 minutes")
                    Text("Price: ₹200")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDriverDialog = false }) {
                    Text("Book Ride")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDriverDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Earnings Dialog
    if (showEarningsDialog) {
        AlertDialog(
            onDismissRequest = { showEarningsDialog = false },
            title = { Text("Driver Earnings") },
            text = {
                Column {
                    Text("Trip History:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Completed trips: 15")
                    Text("• Daily earnings: ₹2,500")
                    Text("• Weekly earnings: ₹15,000")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Export options:")
                    Text("• CSV format")
                    Text("• Graph view")
                }
            },
            confirmButton = {
                TextButton(onClick = { showEarningsDialog = false }) {
                    Text("Export CSV")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEarningsDialog = false }) {
                    Text("Graph View")
                }
            }
        )
    }
}

@Composable
fun RealEstateContent() {
    var showMapDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf("Mumbai") }
    var pinCoordinates by remember { mutableStateOf("19.0760, 72.8777") }

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
                        text = "Real Estate → New → Location Map → Drop Pin",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Location Selection
                    Text(
                        text = "Select Location:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val locations = listOf("Mumbai", "Delhi", "Bangalore", "Chennai", "Kolkata")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        locations.forEach { location ->
                            FilterChip(
                                onClick = { selectedLocation = location },
                                label = { Text(location) },
                                selected = selectedLocation == location
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Map Preview
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Map",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Map Preview")
                            Text("Location: $selectedLocation")
                            Text("Coordinates: $pinCoordinates")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showMapDialog = true }
                        ) {
                            Text("Drop Pin")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Confirm coordinates */ }
                        ) {
                            Text("Confirm")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Auto-fill address */ }
                        ) {
                            Text("Auto-fill")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Property Details
                    Text(
                        text = "Property Details:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val properties = listOf(
                        PropertyInfo("2BHK Apartment", "₹50,00,000", "1200 sq ft", "Ready to move"),
                        PropertyInfo("3BHK Villa", "₹1,20,00,000", "2500 sq ft", "Under construction"),
                        PropertyInfo("1BHK Studio", "₹25,00,000", "600 sq ft", "Furnished")
                    )
                    
                    properties.forEach { property ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Property",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = property.type,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = property.price,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = property.size,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = property.status,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { /* View details */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "View Details"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Map Dialog
    if (showMapDialog) {
        AlertDialog(
            onDismissRequest = { showMapDialog = false },
            title = { Text("Drop Pin on Map") },
            text = {
                Column {
                    Text("Location: $selectedLocation")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Drag pin to adjust location")
                    Text("Current coordinates: $pinCoordinates")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Auto-filled address:")
                    Text("123 Main Street, $selectedLocation")
                }
            },
            confirmButton = {
                TextButton(onClick = { showMapDialog = false }) {
                    Text("Confirm Location")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMapDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class MenuItem(
    val name: String,
    val price: String,
    val note: String
)

data class RideOption(
    val type: String,
    val price: String,
    val car: String,
    val rating: String
)

data class PropertyInfo(
    val type: String,
    val price: String,
    val size: String,
    val status: String
)
