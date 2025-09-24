package com.bijli.live.ui.screens.camera

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
fun CameraScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Photo", "Video", "Group Call")

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
                    text = "📷 Camera",
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
            0 -> PhotoCaptureContent()
            1 -> VideoCaptureContent()
            2 -> GroupCallContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun PhotoCaptureContent() {
    var showCaptionDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var selectedSkinTone by remember { mutableStateOf("Default") }
    var captionText by remember { mutableStateOf("") }

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
                        text = "Take Photo → Add Caption/Text → Emoji Keyboard → Skin Tone Variations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Camera Preview Placeholder
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
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Camera",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Camera Preview")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showCaptionDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Add Caption")
                        }
                        
                        Button(
                            onClick = { showShareDialog = true },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Share")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Skin Tone Selection
                    Text(
                        text = "Skin Tone Variations:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val skinTones = listOf("Default", "Light", "Medium", "Dark")
                        skinTones.forEach { tone ->
                            FilterChip(
                                onClick = { selectedSkinTone = tone },
                                label = { Text(tone) },
                                selected = selectedSkinTone == tone
                            )
                        }
                    }
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Take Photo → Share → Choose Recipient → Recent Chats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Recent Chats
                    Text(
                        text = "Recent Chats:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val recentChats = listOf("John Doe", "Jane Smith", "Family Group", "Work Team")
                    recentChats.forEach { chat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Send to chat */ }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Contact",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = chat,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Preview before sending */ }
                        ) {
                            Text("Preview")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Undo send within 5s */ }
                        ) {
                            Text("Undo Send (5s)")
                        }
                    }
                }
            }
        }
    }

    // Caption Dialog
    if (showCaptionDialog) {
        AlertDialog(
            onDismissRequest = { showCaptionDialog = false },
            title = { Text("Add Caption") },
            text = {
                Column {
                    OutlinedTextField(
                        value = captionText,
                        onValueChange = { captionText = it },
                        label = { Text("Caption") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Emoji Keyboard:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val emojis = listOf("😀", "😂", "❤️", "👍", "🎉", "🔥", "💯", "✨")
                        emojis.forEach { emoji ->
                            Text(
                                text = emoji,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { captionText += emoji }
                                    .background(
                                        Color.LightGray.copy(alpha = 0.3f),
                                        CircleShape
                                    ),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCaptionDialog = false }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCaptionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Share Dialog
    if (showShareDialog) {
        AlertDialog(
            onDismissRequest = { showShareDialog = false },
            title = { Text("Share Photo") },
            text = {
                Column {
                    Text("Choose recipient:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val contacts = listOf("John Doe", "Jane Smith", "Family Group", "Work Team")
                    contacts.forEach { contact ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* Select contact */ }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Contact",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(contact)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showShareDialog = false }) {
                    Text("Send")
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
fun VideoCaptureContent() {
    var showTrimDialog by remember { mutableStateOf(false) }
    var videoLength by remember { mutableStateOf(60) }
    var trimmedLength by remember { mutableStateOf(30) }

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
                        text = "Record Video → Trim Length → Auto-shorten → 30s Preset",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Video Preview Placeholder
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
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Video",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Video Preview")
                            Text("Length: ${videoLength}s")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showTrimDialog = true }
                        ) {
                            Text("Trim Video")
                        }
                        
                        Button(
                            onClick = { trimmedLength = 30 }
                        ) {
                            Text("30s Preset")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Preview clip */ }
                        ) {
                            Text("Preview Clip")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Save trimmed copy */ }
                        ) {
                            Text("Save Copy")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Share trimmed only */ }
                        ) {
                            Text("Share")
                        }
                    }
                }
            }
        }
    }

    // Trim Dialog
    if (showTrimDialog) {
        AlertDialog(
            onDismissRequest = { showTrimDialog = false },
            title = { Text("Trim Video") },
            text = {
                Column {
                    Text("Original Length: ${videoLength}s")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Trimmed Length: ${trimmedLength}s")
                    Slider(
                        value = trimmedLength.toFloat(),
                        onValueChange = { trimmedLength = it.toInt() },
                        valueRange = 5f..videoLength.toFloat(),
                        steps = 4
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { trimmedLength = 15 }
                        ) {
                            Text("15s")
                        }
                        Button(
                            onClick = { trimmedLength = 30 }
                        ) {
                            Text("30s")
                        }
                        Button(
                            onClick = { trimmedLength = 60 }
                        ) {
                            Text("60s")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTrimDialog = false }) {
                    Text("Apply")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTrimDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun GroupCallContent() {
    var showQRDialog by remember { mutableStateOf(false) }
    var linkExpiry by remember { mutableStateOf("1 day") }

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
                        text = "Group Call → Invite → QR Code Share",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Group Call Preview
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
                                imageVector = Icons.Default.Group,
                                contentDescription = "Group Call",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Group Call Preview")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showQRDialog = true }
                        ) {
                            Text("Generate QR")
                        }
                        
                        Button(
                            onClick = { /* Scan QR */ }
                        ) {
                            Text("Scan QR")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Set Expiry:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val expiryOptions = listOf("1 day", "7 days", "No expiry")
                        expiryOptions.forEach { option ->
                            FilterChip(
                                onClick = { linkExpiry = option },
                                label = { Text(option) },
                                selected = linkExpiry == option
                            )
                        }
                    }
                }
            }
        }
    }

    // QR Code Dialog
    if (showQRDialog) {
        AlertDialog(
            onDismissRequest = { showQRDialog = false },
            title = { Text("QR Code Share") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // QR Code Placeholder
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Expiry: $linkExpiry")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Scan via phone camera")
                }
            },
            confirmButton = {
                TextButton(onClick = { showQRDialog = false }) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQRDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
