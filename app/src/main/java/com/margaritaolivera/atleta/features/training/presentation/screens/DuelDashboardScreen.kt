package com.margaritaolivera.atleta.features.training.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.White

@Composable
fun DuelDashboardScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Text("Dashboard de Duelos (Próximamente)", color = White)
    }
}