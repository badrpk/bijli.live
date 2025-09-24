package com.bijli.live.ui.screens.discover

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
fun DiscoverScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Videos", "Moments", "News", "Actions")

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
                    text = "🌟 Discover",
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
            0 -> VideosContent()
            1 -> MomentsContent()
            2 -> NewsContent()
            3 -> PostActionsContent()
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun VideosContent() {
    var isFullscreen by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var brightness by remember { mutableStateOf(0.5f) }

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
                        text = "Videos → Play/Pause → Fullscreen → Enter Fullscreen",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Video Player Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Black)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier.size(48.dp),
                                tint = Color.White
                            )
                            Text(
                                text = if (isPlaying) "Pause" else "Play",
                                color = Color.White
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { isPlaying = !isPlaying }
                        ) {
                            Text(if (isPlaying) "Pause" else "Play")
                        }
                        
                        Button(
                            onClick = { isFullscreen = !isFullscreen }
                        ) {
                            Text(if (isFullscreen) "Exit Fullscreen" else "Enter Fullscreen")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Video Controls
                    Text(
                        text = "Video Controls:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Brightness:")
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = brightness,
                            onValueChange = { brightness = it },
                            modifier = Modifier.weight(1f)
                        )
                        Text("${(brightness * 100).toInt()}%")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { /* Landscape lock */ }
                        ) {
                            Text("Landscape Lock")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Auto brightness */ }
                        ) {
                            Text("Auto Brightness")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Swipe to exit */ }
                        ) {
                            Text("Swipe to Exit")
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
                        text = "Featured Videos:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val featuredVideos = listOf(
                        VideoInfo("Tech Review", "5:30", "1.2M views"),
                        VideoInfo("Cooking Tutorial", "8:15", "856K views"),
                        VideoInfo("Travel Vlog", "12:45", "2.1M views")
                    )
                    
                    featuredVideos.forEach { video ->
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
                                        .size(80.dp)
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = video.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = video.duration,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = video.views,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { /* Play video */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play"
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

@Composable
fun MomentsContent() {
    var commentText by remember { mutableStateOf("") }
    var selectedReaction by remember { mutableStateOf("") }
    var showReactionDialog by remember { mutableStateOf(false) }

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
                        text = "Moments → Comment → Add Text Comment → Emoji Reactions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Comment Input
                    OutlinedTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        label = { Text("Add comment") },
                        placeholder = { Text("Share your thoughts...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Emoji Reactions
                    Text(
                        text = "Emoji Reactions:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val reactions = listOf("👍", "❤️", "😂", "😢", "😡")
                    val reactionNames = listOf("Like", "Love", "Laugh", "Sad", "Angry")
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        reactions.forEachIndexed { index, emoji ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { 
                                    selectedReaction = emoji
                                    showReactionDialog = true
                                }
                            ) {
                                Text(
                                    text = emoji,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = reactionNames[index],
                                    style = MaterialTheme.typography.bodySmall
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
                            onClick = { /* Post comment */ },
                            enabled = commentText.isNotEmpty()
                        ) {
                            Text("Post Comment")
                        }
                        
                        OutlinedButton(
                            onClick = { showReactionDialog = true }
                        ) {
                            Text("React")
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
                        text = "Recent Moments:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val moments = listOf(
                        MomentInfo("John Doe", "Beautiful sunset today! 🌅", "2 hours ago", listOf("👍", "❤️")),
                        MomentInfo("Jane Smith", "Just finished my workout 💪", "4 hours ago", listOf("👍", "👍", "❤️")),
                        MomentInfo("Mike Johnson", "Coffee break ☕", "6 hours ago", listOf("😂", "👍"))
                    )
                    
                    moments.forEach { moment ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = moment.author,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = moment.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = moment.content,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Reactions: ",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    moment.reactions.forEach { reaction ->
                                        Text(
                                            text = reaction,
                                            style = MaterialTheme.typography.bodyMedium
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

    // Reaction Dialog
    if (showReactionDialog) {
        AlertDialog(
            onDismissRequest = { showReactionDialog = false },
            title = { Text("Add Reaction") },
            text = {
                Column {
                    Text("Selected reaction: $selectedReaction")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("React to this moment?")
                }
            },
            confirmButton = {
                TextButton(onClick = { showReactionDialog = false }) {
                    Text("React")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReactionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun NewsContent() {
    var showBookmarkDialog by remember { mutableStateOf(false) }
    var bookmarkedNews by remember { mutableStateOf(setOf<String>()) }

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
                        text = "News → Bookmark → Manage Bookmarks → Delete",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // News Articles
                    Text(
                        text = "Latest News:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val newsArticles = listOf(
                        NewsInfo("Tech Breakthrough", "New AI technology revolutionizes...", "2 hours ago"),
                        NewsInfo("Climate Update", "Global climate initiatives show...", "4 hours ago"),
                        NewsInfo("Sports News", "Championship game results...", "6 hours ago")
                    )
                    
                    newsArticles.forEach { article ->
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
                                        imageVector = Icons.Default.Article,
                                        contentDescription = "News",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.width(12.dp))
                                
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = article.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = article.summary,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = article.time,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = { 
                                        bookmarkedNews = if (article.title in bookmarkedNews) {
                                            bookmarkedNews - article.title
                                        } else {
                                            bookmarkedNews + article.title
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (article.title in bookmarkedNews) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (article.title in bookmarkedNews) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
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
                            onClick = { showBookmarkDialog = true }
                        ) {
                            Text("Manage Bookmarks")
                        }
                        
                        OutlinedButton(
                            onClick = { bookmarkedNews = emptySet() }
                        ) {
                            Text("Clear All")
                        }
                    }
                }
            }
        }
    }

    // Bookmark Dialog
    if (showBookmarkDialog) {
        AlertDialog(
            onDismissRequest = { showBookmarkDialog = false },
            title = { Text("Manage Bookmarks") },
            text = {
                Column {
                    Text("Bookmarked articles:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    if (bookmarkedNews.isEmpty()) {
                        Text("No bookmarks yet")
                    } else {
                        bookmarkedNews.forEach { title ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(title)
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { bookmarkedNews = bookmarkedNews - title }
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
            },
            confirmButton = {
                TextButton(onClick = { showBookmarkDialog = false }) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    bookmarkedNews = emptySet()
                    showBookmarkDialog = false 
                }) {
                    Text("Clear All")
                }
            }
        )
    }
}

@Composable
fun PostActionsContent() {
    var showReportDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("Spam") }

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
                        text = "Post Actions → Report → Reason → Spam",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Sample Post
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Avatar",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "User Name",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                IconButton(
                                    onClick = { showReportDialog = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More options"
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "This is a sample post content that might need moderation.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                IconButton(
                                    onClick = { /* Like */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = "Like"
                                    )
                                }
                                
                                IconButton(
                                    onClick = { /* Comment */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Comment,
                                        contentDescription = "Comment"
                                    )
                                }
                                
                                IconButton(
                                    onClick = { /* Share */ }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share"
                                    )
                                }
                                
                                IconButton(
                                    onClick = { showReportDialog = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Report,
                                        contentDescription = "Report"
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Action Options
                    Text(
                        text = "Available Actions:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { showReportDialog = true }
                        ) {
                            Text("Report")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Block user */ }
                        ) {
                            Text("Block User")
                        }
                        
                        OutlinedButton(
                            onClick = { /* Mute posts */ }
                        ) {
                            Text("Mute Posts")
                        }
                    }
                }
            }
        }
    }

    // Report Dialog
    if (showReportDialog) {
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report Post") },
            text = {
                Column {
                    Text("Select reason:")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val reasons = listOf("Spam", "Inappropriate", "Harassment", "False Information", "Other")
                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedReason = reason }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedReason == reason,
                                onClick = { selectedReason = reason }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(reason)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Report")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class VideoInfo(
    val title: String,
    val duration: String,
    val views: String
)

data class MomentInfo(
    val author: String,
    val content: String,
    val time: String,
    val reactions: List<String>
)

data class NewsInfo(
    val title: String,
    val summary: String,
    val time: String
)
