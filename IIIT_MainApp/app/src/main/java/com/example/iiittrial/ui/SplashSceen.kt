package com.example.iiittrial.ui

import androidx.compose.runtime.Composable
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.iiittrial.MainActivity
import com.example.iiittrial.ui.theme.IiittrialTheme
import com.example.iiittrial.util.Screen
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000)
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route)
    }
    SplashScreen()
}


@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF302C42)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(com.example.iiittrial.R.raw.splashanimation))
        //val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottiefiles.com/113973-share"))
        // Play the animation
        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxWidth().height(500.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF302C42))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "IIIT EVERYTHING",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "Your one-stop destination for your study",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "needs at IIIT Bhubaneswar",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
    }
}}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    SplashScreen()

}