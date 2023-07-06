package com.example.iiittrial.util

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.iiittrial.MainActivity
import com.example.iiittrial.ui.AnimatedSplashScreen
import com.example.iiittrial.ui.MainScreen
import com.example.iiittrial.ui.SplashScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route){
            AnimatedSplashScreen(navController = navController)
        }
        composable(route = Screen.MainScreen.route){
            val activity = LocalContext.current as MainActivity
            MainScreen(application = activity)
        }
    }
}

