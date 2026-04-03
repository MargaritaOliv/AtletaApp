package com.margaritaolivera.atleta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.margaritaolivera.atleta.core.auth.FirebaseAuthManager
import com.margaritaolivera.atleta.core.navigation.AppNavigation
import com.margaritaolivera.atleta.core.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuthManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = if (firebaseAuthManager.currentUser != null) {
            Screens.Home
        } else {
            Screens.Login
        }

        setContent {
            AppNavigation(startDestination = startDestination)
        }
    }
}