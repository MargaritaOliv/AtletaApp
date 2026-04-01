package com.margaritaolivera.atleta.features.training.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.NordicWalking
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.NeonGreen
import com.margaritaolivera.atleta.core.ui.theme.SurfaceDark
import com.margaritaolivera.atleta.core.ui.theme.TextGray
import com.margaritaolivera.atleta.core.ui.theme.White
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStartWorkout: (String) -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.profileState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        TopAppBarAthlete(
            name = profile.name,
            level = profile.level,
            xp = profile.xp,
            maxXp = profile.maxXp,
            onLogoutClick = {
                viewModel.logout()
                onLogout()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "SELECCIONA TU ENTRENAMIENTO",
                    color = TextGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                WorkoutCard(
                    title = "SENTADILLAS",
                    subtitle = "Quema grasa e incrementa fuerza",
                    icon = Icons.Default.FitnessCenter,
                    workoutType = "SQUAT",
                    onClick = { onStartWorkout("SQUAT") }
                )
            }

            item {
                WorkoutCard(
                    title = "CORRER",
                    subtitle = "Cardio de alta intensidad",
                    icon = Icons.Default.DirectionsRun,
                    workoutType = "RUN",
                    onClick = { onStartWorkout("RUN") }
                )
            }

            item {
                WorkoutCard(
                    title = "TROTAR",
                    subtitle = "Calentamiento y resistencia",
                    icon = Icons.Default.NordicWalking,
                    workoutType = "JOG",
                    onClick = { onStartWorkout("JOG") }
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun TopAppBarAthlete(name: String, level: Int, xp: Int, maxXp: Int, onLogoutClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceDark,
        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(NeonGreen.copy(alpha = 0.2f))
                        .border(2.dp, NeonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil", tint = NeonGreen, modifier = Modifier.size(32.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "HOLA, ¡VAMOS!", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = name.uppercase(), color = White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                }

                IconButton(onClick = onLogoutClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Cerrar sesión", tint = White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "NIVEL $level", color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "${xp} / ${maxXp} XP", color = TextGray, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { if (maxXp > 0) xp.toFloat() / maxXp.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = NeonGreen,
                trackColor = DarkBackground
            )
        }
    }
}

@Composable
fun WorkoutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    workoutType: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = subtitle,
                    color = TextGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
