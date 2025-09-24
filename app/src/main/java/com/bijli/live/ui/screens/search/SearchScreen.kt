package com.bijli.live.ui.screens.search

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
fun SearchScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Chats", "Marketplace")

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
                    text = "🔍 Search",
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
            0 -> ChatSearchContent()
            1 -> MarketplaceSearchContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun ChatSearchContent() {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var selectedResult by remember { mutableStateOf(-1) }

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
                        text = "Chats → Search Keyword → Highlight Results",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { 
                            searchQuery = it
                            searchResults = if (it.isNotEmpty()) {
                                listOf(
                                    "Hello, how are you?",
                                    "Meeting at 3 PM today",
                                    "Can you send the file?",
                                    "Thanks for the help!",
                                    "See you tomorrow"
                                ).filter { result -> result.contains(it, ignoreCase = true) }
                            } else {
                                emptyList()
                            }
                        },
                        label = { Text("Search in chats") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Search Results
                    if (searchResults.isNotEmpty()) {
                        Text(
                            text = "Search Results:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        searchResults.forEachIndexed { index, result ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedResult = index }
                                    .background(
                                        if (selectedResult == index) 
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else 
                                            Color.Transparent
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Message,
                                        contentDescription = "Message",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = result,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = { 
                                    if (selectedResult > 0) selectedResult--
                                },
                                enabled = selectedResult > 0
                            ) {
                                Text("Previous")
                            }
                            
                            OutlinedButton(
                                onClick = { 
                                    if (selectedResult < searchResults.size - 1) selectedResult++
                                },
                                enabled = selectedResult < searchResults.size - 1
                            ) {
                                Text("Next")
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Button(
                            onClick = { /* Copy message text */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Copy Message Text")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MarketplaceSearchContent() {
    var selectedCategory by remember { mutableStateOf("Electronics") }
    var selectedSubCategory by remember { mutableStateOf("Mobiles") }

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
                        text = "Marketplace Listings → Category Filter → Electronics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Category Selection
                    Text(
                        text = "Categories:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val categories = listOf("Electronics", "Fashion", "Home", "Sports", "Books")
                    LazyColumn(
                        modifier = Modifier.height(120.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                onClick = { selectedCategory = category },
                                label = { Text(category) },
                                selected = selectedCategory == category,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Electronics Subcategories
                    if (selectedCategory == "Electronics") {
                        Text(
                            text = "Electronics Subcategories:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val electronicsSubcategories = listOf("Mobiles", "Laptops", "Accessories", "Appliances")
                        electronicsSubcategories.forEach { subCategory ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSubCategory = subCategory }
                                    .background(
                                        if (selectedSubCategory == subCategory) 
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        else 
                                            Color.Transparent
                                    ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (subCategory) {
                                            "Mobiles" -> Icons.Default.PhoneAndroid
                                            "Laptops" -> Icons.Default.Laptop
                                            "Accessories" -> Icons.Default.Headphones
                                            "Appliances" -> Icons.Default.Home
                                            else -> Icons.Default.DeviceUnknown
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
                                        text = "12 items",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sample Listings
                    Text(
                        text = "Sample Listings:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val sampleListings = when (selectedSubCategory) {
                        "Mobiles" -> listOf("iPhone 15 Pro", "Samsung Galaxy S24", "OnePlus 12")
                        "Laptops" -> listOf("MacBook Pro M3", "Dell XPS 13", "HP Pavilion")
                        "Accessories" -> listOf("AirPods Pro", "Wireless Charger", "Phone Case")
                        "Appliances" -> listOf("Refrigerator", "Washing Machine", "Microwave")
                        else -> emptyList()
                    }
                    
                    sampleListings.forEach { listing ->
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
                                        contentDescription = "Product Image",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = listing,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "₹25,000",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
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
}
