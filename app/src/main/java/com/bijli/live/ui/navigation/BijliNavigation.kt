package com.bijli.live.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bijli.live.ui.screens.camera.CameraScreen
import com.bijli.live.ui.screens.chats.ChatsScreen
import com.bijli.live.ui.screens.discover.DiscoverScreen
import com.bijli.live.ui.screens.me.MeScreen
import com.bijli.live.ui.screens.plus.PlusScreen
import com.bijli.live.ui.screens.search.SearchScreen
import com.bijli.live.ui.screens.services.ServicesScreen

@Composable
fun BijliNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "camera"
    ) {
        composable("camera") {
            CameraScreen(navController = navController)
        }
        composable("search") {
            SearchScreen(navController = navController)
        }
        composable("plus") {
            PlusScreen(navController = navController)
        }
        composable("chats") {
            ChatsScreen(navController = navController)
        }
        composable("discover") {
            DiscoverScreen(navController = navController)
        }
        composable("services") {
            ServicesScreen(navController = navController)
        }
        composable("me") {
            MeScreen(navController = navController)
        }
    }
}
