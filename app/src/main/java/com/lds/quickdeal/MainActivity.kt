package com.lds.quickdeal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.lds.quickdeal.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MaterialTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LoginScreen()
//                }
//            }
//        }
//    }
//}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MaterialTheme {
                AppNavigation()
            }
        }
    }
}