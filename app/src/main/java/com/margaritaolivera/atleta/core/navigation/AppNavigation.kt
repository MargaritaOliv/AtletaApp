package com.margaritaolivera.atleta.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

// Colores de tu tema
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.NeonGreen

// Pantallas de Auth
import com.margaritaolivera.atleta.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.atleta.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.atleta.features.auth.presentation.viewmodels.AuthViewModel

// Pantallas de Training
import com.margaritaolivera.atleta.features.training.presentation.screens.HomeScreen
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.HomeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Login
    ) {

        // --- MÓDULO A: AUTENTICACIÓN ---

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

        // --- MÓDULO B: ENTRENAMIENTOS ---

        composable<Screens.Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onStartWorkout = { workoutType ->
                    navController.navigate(Screens.WorkoutSession(type = workoutType))
                }
            )
        }

        composable<Screens.WorkoutSession> { backStackEntry ->
            val session = backStackEntry.toRoute<Screens.WorkoutSession>()
            val type = session.type

            // Pantalla temporal de entrenamiento (Hasta que creemos la WorkoutSessionScreen)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ENTRENAMIENTO: $type",
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }

        // --- MÓDULO C: SOCIAL ---

        composable<Screens.FriendsList> {
            // Placeholder
        }

        composable<Screens.Ranking> {
            // Placeholder
        }

        // --- TIEMPO REAL: DUELOS ---

        composable<Screens.LiveDuel> { backStackEntry ->
            val duel = backStackEntry.toRoute<Screens.LiveDuel>()
            val room = duel.roomName
            // Placeholder
        }
    }
}