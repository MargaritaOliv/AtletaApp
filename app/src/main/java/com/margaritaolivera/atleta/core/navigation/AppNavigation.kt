package com.margaritaolivera.atleta.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.NeonGreen
import com.margaritaolivera.atleta.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.atleta.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.atleta.features.auth.presentation.viewmodels.AuthViewModel
import com.margaritaolivera.atleta.features.social.presentation.screens.SocialScreen
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.SocialViewModel
import com.margaritaolivera.atleta.features.training.presentation.screens.HomeScreen
import com.margaritaolivera.atleta.features.training.presentation.screens.WorkoutSessionScreen
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.HomeViewModel
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.WorkoutSessionViewModel

@Composable
fun AppNavigation(startDestination: Any) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
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
            val viewModel: HomeViewModel = hiltViewModel()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            LaunchedEffect(navBackStackEntry) {
                viewModel.refreshProfile()
            }
            HomeScreen(
                viewModel = viewModel,
                onStartWorkout = { workoutType ->
                    navController.navigate(Screens.WorkoutSession(type = workoutType))
                },
                onNavigateToSocial = {
                    navController.navigate(Screens.Social)
                },
                onLogout = {
                    navController.navigate(Screens.Login) {
                        popUpTo<Screens.Home> { inclusive = true }
                    }
                }
            )
        }

        composable<Screens.WorkoutSession> { backStackEntry ->
            val session = backStackEntry.toRoute<Screens.WorkoutSession>()
            val viewModel: WorkoutSessionViewModel = hiltViewModel()

            WorkoutSessionScreen(
                viewModel = viewModel,
                workoutType = session.type,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screens.Social> {
            val viewModel: SocialViewModel = hiltViewModel()
            SocialScreen(viewModel = viewModel)
        }

        composable<Screens.LiveDuel> { backStackEntry ->
            val duel = backStackEntry.toRoute<Screens.LiveDuel>()
            val room = duel.roomName
        }
    }
}