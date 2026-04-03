package com.margaritaolivera.atleta.features.training.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.WorkoutSessionViewModel

@Composable
fun WorkoutSessionScreen(
    viewModel: WorkoutSessionViewModel,
    workoutType: String,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startWorkout(workoutType)
    }

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading && !state.isTracking) {
            CircularProgressIndicator(color = NeonGreen)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Preparando entrenamiento...", color = TextGray)
        } else {
            Text(
                text = workoutType.uppercase(),
                fontSize = 24.sp,
                color = TextGray,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "${state.currentReps}",
                fontSize = 120.sp,
                color = White,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "REPETICIONES ACTUALES",
                fontSize = 16.sp,
                color = NeonGreen,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Total guardadas: ${state.totalReps}",
                fontSize = 14.sp,
                color = TextGray
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = { viewModel.saveSet() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("GUARDAR SERIE", color = NeonGreen, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.finishWorkout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("FINALIZAR ENTRENAMIENTO", color = DarkBackground, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}