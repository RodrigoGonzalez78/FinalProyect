package com.example.finalproyect.presenter.navigator

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.finalproyect.presenter.event_detail.EventDetailScreen
import com.example.finalproyect.presenter.home.HomeScreen
import com.example.finalproyect.presenter.login.LoginScreen
import com.example.finalproyect.presenter.new_event.NewEventScreen
import com.example.finalproyect.presenter.profile.ProfileScreen
import com.example.finalproyect.presenter.register.RegisterScreen
import com.example.finalproyect.presenter.scanner.ScannerScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Register : Screen("signup")
    data object Login : Screen("login")
    data object NewEvent : Screen("mew_event")
    data object Profile : Screen("profile")
    data object EventDetails:Screen("event_details?eventId={eventId}"){
        fun createRoute(eventId: String) = "event_details?eventId=$eventId"
    }
    data object UsersProfiles : Screen("users_profiles?userId={userId}") {
        fun createRoute(userId: String) = "users_profiles?userId=$userId"
    }

    data object Scanner :
        Screen("scanner?eventId={eventId}&eventName={eventName}") {

        /**
         * Construye la ruta de navegación codificando el nombre del evento
         * para evitar problemas con espacios o caracteres especiales.
         */
        fun createRoute(eventId: Int, eventName: String): String =
            "scanner?eventId=$eventId&eventName=${Uri.encode(eventName)}"
    }
}

@androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
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


        composable(Screen.NewEvent.route)  {
            NewEventScreen(
                navController
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController
            )
        }

        composable(Screen.EventDetails.route, arguments = listOf(
            navArgument("eventId") {
                type = NavType.StringType
                nullable = true
            }
        )) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            Log.e("Error",eventId.toString())
            EventDetailScreen(
                navController,
                eventId = eventId?:""
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(
            Screen.Scanner.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("eventName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            val eventName = backStackEntry.arguments?.getString("eventName") ?: ""

            ScannerScreen(
                eventId = eventId,
                eventName = eventName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}