package com.example.iiittrial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.iiittrial.ui.MainScreen
import com.example.iiittrial.ui.theme.IiittrialTheme
import com.example.iiittrial.util.SetupNavGraph

class MainActivity : ComponentActivity() {

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IiittrialTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController = navController)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IiittrialTheme {
        MainScreen(application = MainActivity())
    }
}