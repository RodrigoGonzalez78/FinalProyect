package com.example.finalproyect.presenter.navigator
import android.os.Build
import android.util.Log
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
import androidx.navigation.toRoute
import com.example.finalproyect.presenter.event_detail.EventDetailScreen
import com.example.finalproyect.presenter.home.HomeScreen
import com.example.finalproyect.presenter.login.LoginScreen
import com.example.finalproyect.presenter.new_event.NewEventScreen
import com.example.finalproyect.presenter.profile.ProfileScreen
import com.example.finalproyect.presenter.register.RegisterScreen
import com.example.finalproyect.presenter.scanner.ScannerScreen
import kotlinx.serialization.Serializable


@Serializable
sealed interface AppDestination {

    @Serializable
    data object Splash : AppDestination

    @Serializable
    data object Home : AppDestination

    @Serializable
    data object Register : AppDestination

    @Serializable
    data object Login : AppDestination

    @Serializable
    data object NewEvent : AppDestination

    @Serializable
    data object Profile : AppDestination

    @Serializable
    data class EventDetails(
        val eventId: String
    ) : AppDestination

    @Serializable
    data class TicketsDetails(
        val eventId: String
    ) : AppDestination




    @Serializable
    data class Scanner(
        val eventId: Int,
        val eventName: String
    ) : AppDestination
}


fun NavHostController.navigateToEventDetails(eventId: String) {
    navigate(AppDestination.EventDetails(eventId))
}

fun NavHostController.navigateToScanner(eventId: Int, eventName: String) {
    navigate(AppDestination.Scanner(eventId, eventName))
}

fun NavHostController.navigateToHome() {
    navigate(AppDestination.Home) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavHostController.navigateToLogin() {
    navigate(AppDestination.Login) {
        popUpTo(0) { inclusive = true }
    }
}

@androidx.annotation.RequiresPermission(
    allOf = [
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    ]
)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavigationViewModel = hiltViewModel()
) {
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigateToHome()
        } else {
            navController.navigateToLogin()
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppDestination.Login
    ) {

        composable<AppDestination.Login> {
            LoginScreen(navController)
        }

        composable<AppDestination.Register> {
            RegisterScreen(navController)
        }

        composable<AppDestination.NewEvent> {
            NewEventScreen(navController)
        }

        composable<AppDestination.Profile> {
            ProfileScreen(navController)
        }

        composable<AppDestination.EventDetails> { backStackEntry ->
            val eventDetails: AppDestination.EventDetails = backStackEntry.toRoute()
            EventDetailScreen(
                navController = navController,
                eventId = eventDetails.eventId
            )


        }

        composable<AppDestination.Home> {
            HomeScreen(navController)
        }
        composable<AppDestination.Scanner> { backStackEntry ->
            val scanner: AppDestination.Scanner = backStackEntry.toRoute()

            ScannerScreen(
                eventId = scanner.eventId,
                eventName = scanner.eventName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}


