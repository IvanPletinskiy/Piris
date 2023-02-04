@file:OptIn(ExperimentalMaterialApi::class)

package com.handen.piris

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.handen.piris.ui.theme.PirisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        setContent {
            setContent {
                CompositionLocalProvider(
                    LocalNavController provides rememberNavController()
                ) {
                    PirisTheme {
                        NavHost(
                            navController = LocalNavController.current,
                            startDestination = "list",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("list") { MainScreen(viewModel) }
                            composable("edit") {
                                EditScreen(viewModel)
                            }
                            composable("info") {
                                InfoScreen(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    throw IllegalStateException("LocalNavController is null")
}