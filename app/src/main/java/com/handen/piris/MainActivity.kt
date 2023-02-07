@file:OptIn(ExperimentalMaterialApi::class)

package com.handen.piris

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.handen.piris.accounts.AccountsScreen
import com.handen.piris.ui.theme.PirisTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        context = this
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()

        setContent {
            setContent {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CompositionLocalProvider(
                        LocalNavController provides rememberNavController()
                    ) {
                        val scope = rememberCoroutineScope()
                        val context = LocalContext.current
                        val navController = LocalNavController.current
                        LaunchedEffect(Unit) {
                            scope.launch {
                                viewModel.uiEvents.collect() {
                                    when (it) {
                                        is MainViewModel.UiEvent.NavigateBack -> navController.popBackStack()
                                        is MainViewModel.UiEvent.Toast -> {
                                            Toast.makeText(context, it.string, Toast.LENGTH_LONG)
                                                .show()
                                        }
                                    }
                                }
                            }
                        }
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
                                composable("create_deposit") {
                                    CreateDepositScreen(viewModel)
                                }
                                composable("accounts") {
                                    AccountsScreen(viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        var context: Context? = null
    }
}

val LocalNavController = staticCompositionLocalOf<NavHostController> {
    throw IllegalStateException("LocalNavController is null")
}