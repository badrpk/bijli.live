package com.bijli.live.ui.screens.plus

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
fun PlusScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Add Contact", "New Group", "Broadcast", "Devices")

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
                    text = "＋ Plus",
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
            0 -> AddContactContent()
            1 -> NewGroupContent()
            2 -> BroadcastContent()
            3 -> DevicesContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun AddContactContent() {
    var phoneNumber by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

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
                        text = "Add Contact → Enter Phone → Validate → Error Message",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Phone Number Input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+91 9876543210") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber)
                    )
                    
                    if (phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber)) {
                        Text(
                            text = "Invalid format",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (isValidPhoneNumber(phoneNumber)) {
                                    // Add contact logic
                                } else {
                                    errorMessage = "Invalid format"
                                    showErrorDialog = true
                                }
                            },
                            enabled = phoneNumber.isNotEmpty()
                        ) {
                            Text("Validate")
                        }
                        
                        OutlinedButton(
                            onClick = { phoneNumber = "" }
                        ) {
                            Text("Clear")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Suggestions
                    Text(
                        text = "Suggestions:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val suggestions = listOf(
                        "Use country code (+91 for India)",
                        "Include area code",
                        "Remove spaces and special characters",
                        "Minimum 10 digits required"
                    )
                    
                    suggestions.forEach { suggestion ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Suggestion",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = {
                Column {
                    Text(errorMessage)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Suggest fix:")
                    Text("• Use country code (+91 for India)")
                    Text("• Include area code")
                    Text("• Remove spaces and special characters")
                }
            },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Try Again")
                }
            },
            dismissButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NewGroupContent() {
    var groupName by remember { mutableStateOf("") }
    var linkExpiry by remember { mutableStateOf("24h") }
    var showLinkDialog by remember { mutableStateOf(false) }

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
                        text = "New Group → Invite via Link → Expiry",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Group Name Input
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Group Name") },
                        placeholder = { Text("Enter group name") },
                        leadingIcon = { Icon(Icons.Default.Group, contentDescription = "Group") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Link Expiry Options
                    Text(
                        text = "Link Expiry:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val expiryOptions = listOf("24h", "7 days", "No expiry")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        expiryOptions.forEach { option ->
                            FilterChip(
                                onClick = { linkExpiry = option },
                                label = { Text(option) },
                                selected = linkExpiry == option
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showLinkDialog = true },
                            enabled = groupName.isNotEmpty()
                        ) {
                            Text("Create Group")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Cancel link */ }
                        ) {
                            Text("Cancel Link")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Group Preview
                    if (groupName.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Group,
                                    contentDescription = "Group",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = groupName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Link expires: $linkExpiry",
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

    // Link Dialog
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = { showLinkDialog = false },
            title = { Text("Group Created") },
            text = {
                Column {
                    Text("Group: $groupName")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Invite Link: https://bijli.live/group/abc123")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Expires: $linkExpiry")
                }
            },
            confirmButton = {
                TextButton(onClick = { showLinkDialog = false }) {
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
fun BroadcastContent() {
    var messageText by remember { mutableStateOf("") }
    var showMediaDialog by remember { mutableStateOf(false) }
    var selectedMedia by remember { mutableStateOf("Image") }

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
                        text = "Broadcast Lists → Send Message → Attach Media → Image",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Message Input
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("Message") },
                        placeholder = { Text("Type your broadcast message") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Media Attachment
                    Text(
                        text = "Attach Media:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showMediaDialog = true }
                        ) {
                            Text("Add Image")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Add video */ }
                        ) {
                            Text("Add Video")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Add document */ }
                        ) {
                            Text("Add Document")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Media Preview
                    if (selectedMedia == "Image") {
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
                                
                                Column {
                                    Text(
                                        text = "Image attached",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Compress quality: Medium",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                IconButton(
                                    onClick = { /* Remove media */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove"
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
                        OutlinedButton(
                            onClick = { /* Preview before send */ }
                        ) {
                            Text("Preview")
                        }
                        
                        Button(
                            onClick = { /* Send broadcast */ },
                            enabled = messageText.isNotEmpty()
                        ) {
                            Text("Send Broadcast")
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
            title = { Text("Attach Media") },
            text = {
                Column {
                    Text("Select media type:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val mediaTypes = listOf("Image", "Video", "Document", "Audio")
                    mediaTypes.forEach { type ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedMedia = type }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (type) {
                                    "Image" -> Icons.Default.Image
                                    "Video" -> Icons.Default.Videocam
                                    "Document" -> Icons.Default.Description
                                    "Audio" -> Icons.Default.AudioFile
                                    else -> Icons.Default.AttachFile
                                },
                                contentDescription = type,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(type)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showMediaDialog = false }) {
                    Text("Select")
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
fun DevicesContent() {
    var showDeviceDialog by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf("") }

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
                        text = "Linked Devices → Active Sessions → Device List → Device Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Active Sessions
                    Text(
                        text = "Active Sessions:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val devices = listOf(
                        DeviceInfo("iPhone", "iOS 17.1", "Last seen: 2 minutes ago", Icons.Default.PhoneIphone),
                        DeviceInfo("Android", "Android 14", "Last seen: 5 minutes ago", Icons.Default.PhoneAndroid),
                        DeviceInfo("Desktop Browser", "Chrome 119", "Last seen: 1 hour ago", Icons.Default.Computer)
                    )
                    
                    devices.forEach { device ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { 
                                    selectedDevice = device.name
                                    showDeviceDialog = true 
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = device.icon,
                                    contentDescription = device.name,
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = device.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = device.version,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = device.lastSeen,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { /* Add new device */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Link New Device")
                    }
                }
            }
        }
    }

    // Device Dialog
    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Device Details") },
            text = {
                Column {
                    Text("Device: $selectedDevice")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Status: Active")
                    Text("Last sync: Just now")
                    Text("Battery: 85%")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDeviceDialog = false }) {
                    Text("Disconnect")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeviceDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

data class DeviceInfo(
    val name: String,
    val version: String,
    val lastSeen: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

fun isValidPhoneNumber(phone: String): Boolean {
    val cleanPhone = phone.replace(Regex("[^0-9+]"), "")
    return cleanPhone.length >= 10 && cleanPhone.startsWith("+")
}
