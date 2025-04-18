package com.example.finalproyect.presenter.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalproyect.presenter.login.LoginScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Signup : Screen("signup")
    data object Login : Screen("login")
    data object NewTweet : Screen("mew_tweet")
    data object EditProfile : Screen("edit_profile")
    data object UsersProfiles : Screen("users_profiles?userId={userId}") {
        fun createRoute(userId: String) = "users_profiles?userId=$userId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        val route = if (isAuthenticated) Screen.Home.route else Screen.Login.route
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

    }
}