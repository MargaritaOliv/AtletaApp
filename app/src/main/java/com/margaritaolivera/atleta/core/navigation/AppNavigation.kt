package com.margaritaolivera.atleta.core.navigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.margaritaolivera.atleta.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.atleta.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.atleta.features.auth.presentation.viewmodels.AuthViewModel



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Login
    ) {



        composable<Screens.Login> {
            val viewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Screens.Home) {
                        popUpTo<Screens.Login> { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screens.Register)
                }
            )
        }

        composable<Screens.Register> {
            val viewModel: AuthViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }


        composable<Screens.Home> {
            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text("Bienvenido al Dashboard del Atleta")
            }
        }

        composable<Screens.WorkoutSession> { backStackEntry ->
            val workout = backStackEntry.toRoute<Screens.WorkoutSession>()
            val type = workout.type

        }



        composable<Screens.FriendsList> {
        }

        composable<Screens.Ranking> {
        }


        composable<Screens.LiveDuel> { backStackEntry ->
            val duel = backStackEntry.toRoute<Screens.LiveDuel>()
            val roomName = duel.roomName
        }
    }
}