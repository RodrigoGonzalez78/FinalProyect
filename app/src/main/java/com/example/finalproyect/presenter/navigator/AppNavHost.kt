package com.example.finalproyect.presenter.navigator

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalproyect.presenter.home.HomeScreen
import com.example.finalproyect.presenter.login.LoginScreen
import com.example.finalproyect.presenter.new_event.NewEventScreen
import com.example.finalproyect.presenter.register.RegisterScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Register : Screen("signup")
    data object Login : Screen("login")
    data object NewEvent : Screen("mew_event")
    data object EditProfile : Screen("edit_profile")
    data object UsersProfiles : Screen("users_profiles?userId={userId}") {
        fun createRoute(userId: String) = "users_profiles?userId=$userId"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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


        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.NewEvent.route) {
            NewEventScreen(
                navController
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
    }
}