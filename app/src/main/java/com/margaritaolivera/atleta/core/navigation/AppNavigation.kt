package com.margaritaolivera.atleta.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.margaritaolivera.atleta.core.ui.components.AtletaBottomBar
import com.margaritaolivera.atleta.features.auth.presentation.screens.LoginScreen
import com.margaritaolivera.atleta.features.auth.presentation.screens.RegisterScreen
import com.margaritaolivera.atleta.features.auth.presentation.viewmodels.AuthViewModel
import com.margaritaolivera.atleta.features.social.presentation.screens.DuelSelectionScreen
import com.margaritaolivera.atleta.features.social.presentation.screens.RankingScreen
import com.margaritaolivera.atleta.features.social.presentation.screens.SocialScreen
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.DuelViewModel
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.SocialViewModel
import com.margaritaolivera.atleta.features.training.presentation.screens.DuelDashboardScreen
import com.margaritaolivera.atleta.features.training.presentation.screens.HomeScreen
import com.margaritaolivera.atleta.features.training.presentation.screens.WorkoutSessionScreen
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.HomeViewModel
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.WorkoutSessionViewModel

@Composable
fun AppNavigation(startDestination: Any) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hierarchy?.any { destination ->
        destination.hasRoute<Screens.Home>() ||
                destination.hasRoute<Screens.Duel>() ||
                destination.hasRoute<Screens.Ranking>() ||
                destination.hasRoute<Screens.Social>()
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AtletaBottomBar(
                    currentRoute = currentDestination,
                    onNavigateToHome = {
                        navController.navigate(Screens.Home) {
                            popUpTo(Screens.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToDuel = {
                        navController.navigate(Screens.Duel) {
                            popUpTo(Screens.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToRanking = {
                        navController.navigate(Screens.Ranking) {
                            popUpTo(Screens.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToSocial = {
                        navController.navigate(Screens.Social) {
                            popUpTo(Screens.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screens.Login> {
                val viewModel: AuthViewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()

                LoginScreen(
                    state = state,
                    onEmailChange = viewModel::onEmailLoginChange,
                    onPasswordChange = viewModel::onPasswordLoginChange,
                    onLoginClick = viewModel::loginAndSync,
                    onLoginSuccess = {
                        navController.navigate(Screens.Home) {
                            popUpTo<Screens.Login> { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screens.Register)
                    },
                    onResetState = viewModel::resetState
                )
            }

            composable<Screens.Register> {
                val viewModel: AuthViewModel = hiltViewModel()
                val state by viewModel.state.collectAsState()

                RegisterScreen(
                    state = state,
                    onNameChange = viewModel::onNameRegisterChange,
                    onEmailChange = viewModel::onEmailRegisterChange,
                    onPasswordChange = viewModel::onPasswordRegisterChange,
                    onRegisterClick = viewModel::registerAndSync,
                    onRegisterSuccess = {
                        navController.popBackStack()
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    },
                    onResetState = viewModel::resetState
                )
            }

            composable<Screens.Home> {
                val viewModel: HomeViewModel = hiltViewModel()
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
                    onNavigateToDuel = {
                        navController.navigate(Screens.DuelSelection)
                    },
                    onLogout = {
                        navController.navigate(Screens.Login) {
                            popUpTo<Screens.Home> { inclusive = true }
                        }
                    }
                )
            }

            composable<Screens.Duel> {
                DuelDashboardScreen()
            }

            composable<Screens.DuelSelection> {
                val viewModel: DuelViewModel = hiltViewModel()
                DuelSelectionScreen(
                    viewModel = viewModel,
                    onNavigateToWorkout = { type, name, xp ->
                        navController.navigate(
                            Screens.WorkoutSession(
                                type = type,
                                opponentName = name,
                                opponentXp = xp
                            )
                        )
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<Screens.Ranking> {
                val viewModel: SocialViewModel = hiltViewModel()
                RankingScreen(viewModel = viewModel)
            }

            composable<Screens.Social> {
                val viewModel: SocialViewModel = hiltViewModel()
                SocialScreen(viewModel = viewModel)
            }

            composable<Screens.WorkoutSession> { backStackEntry ->
                val session = backStackEntry.toRoute<Screens.WorkoutSession>()
                val viewModel: WorkoutSessionViewModel = hiltViewModel()

                WorkoutSessionScreen(
                    viewModel = viewModel,
                    workoutType = session.type,
                    opponentName = session.opponentName,
                    opponentXp = session.opponentXp,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}