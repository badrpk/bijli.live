package com.bijli.live.ui.screens.chats

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
fun ChatsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Recents", "Saved Rooms", "Support")

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
                    text = "💬 Chats",
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
            0 -> RecentsContent()
            1 -> SavedRoomsContent()
            2 -> SupportContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun RecentsContent() {
    var showMediaDialog by remember { mutableStateOf(false) }
    var selectedMedia by remember { mutableStateOf("Photo") }

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
                        text = "Recents → Open Chat → Send Media → Photo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Recent Chats List
                    Text(
                        text = "Recent Chats:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val recentChats = listOf(
                        ChatInfo("John Doe", "Hey, how are you?", "2 min ago", true),
                        ChatInfo("Jane Smith", "Thanks for the help!", "5 min ago", false),
                        ChatInfo("Family Group", "Dinner at 7 PM", "10 min ago", true),
                        ChatInfo("Work Team", "Meeting tomorrow at 9 AM", "1 hour ago", false)
                    )
                    
                    recentChats.forEach { chat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showMediaDialog = true },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            if (chat.isOnline) Color.Green.copy(alpha = 0.3f) 
                                            else Color.Gray.copy(alpha = 0.3f)
                                        )
                                        .clip(RoundedCornerShape(24.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = chat.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = chat.lastMessage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    Text(
                                        text = chat.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (chat.isOnline) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .background(Color.Green, RoundedCornerShape(4.dp))
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Media Options
                    Text(
                        text = "Send Media Options:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { selectedMedia = "Take New Photo" }
                        ) {
                            Text("Take Photo")
                        }
                        
                        OutlinedButton(
                            onClick = { selectedMedia = "Upload from Gallery" }
                        ) {
                            Text("Gallery")
                        }
                        
                        OutlinedButton(
                            onClick = { selectedMedia = "Forward from Chat" }
                        ) {
                            Text("Forward")
                        }
                    }
                }
            }
        }
    }

    // Media Dialog
    if (showMediaDialog) {
        AlertDialog(
            onDismissRequest = { showMediaDialog = false },
            title = { Text("Send Media") },
            text = {
                Column {
                    Text("Selected: $selectedMedia")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    when (selectedMedia) {
                        "Take New Photo" -> {
                            Text("• Open camera")
                            Text("• Take photo")
                            Text("• Add caption")
                        }
                        "Upload from Gallery" -> {
                            Text("• Select from gallery")
                            Text("• Choose multiple images")
                            Text("• Add caption")
                        }
                        "Forward from Chat" -> {
                            Text("• Select chat")
                            Text("• Choose message")
                            Text("• Forward to current chat")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMediaDialog = false }) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showMediaDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SavedRoomsContent() {
    var showLinkDialog by remember { mutableStateOf(false) }
    var linkCopied by remember { mutableStateOf(false) }

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
                        text = "Saved Rooms → Share Invite → Copy Link → Confirm Copied",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Saved Rooms List
                    Text(
                        text = "Saved Rooms:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val savedRooms = listOf(
                        RoomInfo("Tech Discussion", "15 members", "Last active: 1 hour ago"),
                        RoomInfo("Book Club", "8 members", "Last active: 2 hours ago"),
                        RoomInfo("Travel Plans", "12 members", "Last active: 3 hours ago"),
                        RoomInfo("Study Group", "6 members", "Last active: 1 day ago")
                    )
                    
                    savedRooms.forEach { room ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showLinkDialog = true },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = "Room",
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = room.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = room.members,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = room.lastActive,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { showLinkDialog = true }
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
                            onClick = { 
                                linkCopied = true
                                showLinkDialog = true 
                            }
                        ) {
                            Text("Copy Link")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Share directly */ }
                        ) {
                            Text("Share Directly")
                        }
                    }
                    
                    if (linkCopied) {
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
                                    contentDescription = "Copied",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Link copied to clipboard",
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    onClick = { linkCopied = false }
                                ) {
                                    Text("Undo")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Link Dialog
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text("Share Room Invite") },
            text = {
                Column {
                    Text("Room: Tech Discussion")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Invite Link: https://bijli.live/room/tech123")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Expires: 7 days")
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showLinkDialog = false
                    linkCopied = true
                }) {
                    Text("Copy Link")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLinkDialog = false }) {
                    Text("Share")
                }
            }
        )
    }
}

@Composable
fun SupportContent() {
    var selectedTopic by remember { mutableStateOf("Payments") }
    var showFAQDialog by remember { mutableStateOf(false) }

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
                        text = "Support → FAQs → Browse Topics → Payments",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // FAQ Topics
                    Text(
                        text = "Browse Topics:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val topics = listOf("Payments", "Account", "Privacy", "Technical", "General")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        topics.forEach { topic ->
                            FilterChip(
                                onClick = { selectedTopic = topic },
                                label = { Text(topic) },
                                selected = selectedTopic == topic
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Payment FAQs
                    if (selectedTopic == "Payments") {
                        Text(
                            text = "Payment FAQs:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val paymentFAQs = listOf(
                            FAQItem("How escrow works", "Escrow protects both buyers and sellers..."),
                            FAQItem("Refund process", "Refunds are processed within 3-5 business days..."),
                            FAQItem("Fees explained", "We charge a small fee for secure transactions...")
                        )
                        
                        paymentFAQs.forEach { faq ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showFAQDialog = true },
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    Text(
                                        text = faq.question,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = faq.answer,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                            onClick = { /* Contact support */ }
                        ) {
                            Text("Contact Support")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Live chat */ }
                        ) {
                            Text("Live Chat")
                        }
                    }
                }
            }
        }
    }

    // FAQ Dialog
    if (showFAQDialog) {
        AlertDialog(
            onDismissRequest = { showFAQDialog = false },
            title = { Text("FAQ Details") },
            text = {
                Column {
                    Text("Question: How escrow works")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Answer: Escrow protects both buyers and sellers by holding funds securely until the transaction is completed. The money is released only when both parties are satisfied.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Was this helpful?")
                }
            },
            confirmButton = {
                TextButton(onClick = { showFAQDialog = false }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFAQDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

data class ChatInfo(
    val name: String,
    val lastMessage: String,
    val time: String,
    val isOnline: Boolean
)

data class RoomInfo(
    val name: String,
    val members: String,
    val lastActive: String
)

data class FAQItem(
    val question: String,
    val answer: String
)
