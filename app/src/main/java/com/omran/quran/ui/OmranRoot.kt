package com.omran.quran.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.omran.quran.data.model.Session
import com.omran.quran.ui.components.LoadingBox
import com.omran.quran.ui.navigation.OmranNavHost
import com.omran.quran.ui.navigation.Routes
import com.omran.quran.ui.theme.OmranTheme

@Composable
fun OmranRoot(viewModel: AppViewModel = hiltViewModel()) {
    val session by viewModel.session.collectAsStateWithLifecycle()
    val darkPref by viewModel.darkTheme.collectAsStateWithLifecycle()
    val dark = darkPref ?: isSystemInDarkTheme()
    val navController = rememberNavController()

    OmranTheme(darkTheme = dark) {
        when (session) {
            Session.Loading -> LoadingBox()
            else -> {
                val start = if (session is Session.SignedIn) Routes.MAIN else Routes.LOGIN
                OmranNavHost(startDestination = start, navController = navController)
            }
        }
    }

    LaunchedEffect(session) {
        when (session) {
            is Session.SignedIn -> {
                val current = navController.currentDestination?.route
                if (current != Routes.MAIN && current != Routes.SETTINGS &&
                    current?.startsWith("profile") != true &&
                    current?.startsWith("student") != true &&
                    current?.startsWith("record") != true
                ) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            Session.SignedOut -> {
                val route = navController.currentDestination?.route
                if (route != null && route != Routes.LOGIN && route != Routes.REGISTER) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            Session.Loading -> Unit
        }
    }
}
