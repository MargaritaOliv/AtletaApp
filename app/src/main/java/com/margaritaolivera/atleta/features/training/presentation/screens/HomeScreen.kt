package com.margaritaolivera.atleta.features.training.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.NordicWalking
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
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.HomeViewModel
import java.time.LocalTime

val CyanAccent = Color(0xFF00FFD1)
val CardDarkBackground = Color(0xFF141414)
val DarkGrayText = Color(0xFF555555)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onStartWorkout: (String) -> Unit,
    onNavigateToSocial: () -> Unit,
    onNavigateToDuel: () -> Unit,
    onLogout: () -> Unit
) {
    val profile by viewModel.profileState.collectAsState()

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                onNavigateToSocial = onNavigateToSocial,
                onNavigateToDuel = onNavigateToDuel,
                hasPendingRequests = profile.hasPendingRequests
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(40.dp)) }

            item {
                HeaderSection(
                    fullName = profile.name,
                    onLogout = {
                        viewModel.logout()
                        onLogout()
                    }
                )
            }

            item {
                HeroStatsCard(
                    level = profile.level,
                    xp = profile.xp,
                    maxXp = profile.maxXp
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val distText = if (profile.lastRunKm > 0f) "%.2f".format(profile.lastRunKm) else "--"
                    val squatText = if (profile.lastSquatReps > 0) "${profile.lastSquatReps}" else "--"
                    StatSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "DISTANCIA",
                        value = distText,
                        unit = if (profile.lastRunKm > 0f) " km" else "",
                        valueColor = CyanAccent
                    )
                    StatSquareCard(
                        modifier = Modifier.weight(1f),
                        title = "SENTADILLAS",
                        value = squatText,
                        unit = if (profile.lastSquatReps > 0) " reps" else "",
                        valueColor = White
                    )
                }
            }

            item {
                val wonText = if (profile.duelsTotal > 0) profile.duelsWon.toString() else "--"
                val totalText = if (profile.duelsTotal > 0) profile.duelsTotal.toString() else "--"
                DuelsCard(won = wonText, total = totalText)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "INICIAR ENTRENAMIENTO",
                    color = DarkGrayText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                WorkoutActionCard(
                    icon = Icons.Default.FitnessCenter,
                    title = "SQUAT",
                    subtitle = "Sentadillas",
                    details = "Incrementa tu fuerza",
                    onClick = { onStartWorkout("SQUAT") }
                )
            }

            item {
                WorkoutActionCard(
                    icon = Icons.Default.DirectionsRun,
                    title = "RUN",
                    subtitle = "Carrera",
                    details = "Cardio de alta intensidad",
                    onClick = { onStartWorkout("RUN") }
                )
            }

            item {
                WorkoutActionCard(
                    icon = Icons.Default.NordicWalking,
                    title = "JOG",
                    subtitle = "Trote",
                    details = "Resistencia y calentamiento",
                    onClick = { onStartWorkout("JOG") }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HeaderSection(fullName: String, onLogout: () -> Unit) {
    val nameParts = fullName.trim().split(" ")
    val firstName = nameParts.firstOrNull() ?: "Atleta"
    val lastName = if (nameParts.size > 1) nameParts.drop(1).joinToString(" ") else ""

    val initials = "${firstName.take(1)}${lastName.take(1)}".uppercase()

    val currentHour = LocalTime.now().hour
    val timeBasedPhrase = when (currentHour) {
        in 5..11 -> "¡Buenos días! A entrenar."
        in 12..18 -> "¡Buenas tardes! Sigue así."
        else -> "¡Buenas noches! Cierra con fuerza."
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Bienvenido", color = TextGray, fontSize = 14.sp)
            Row {
                Text(text = "$firstName ", color = White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                if (lastName.isNotEmpty()) {
                    Text(text = lastName, color = NeonGreen, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timeBasedPhrase,
                color = CyanAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(2.dp, NeonGreen, CircleShape)
                .background(Color(0xFF1A1A00))
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

@Composable
fun HeroStatsCard(level: Int, xp: Int, maxXp: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NeonGreen, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(text = "Nivel $level", textColor = NeonGreen, borderColor = NeonGreen)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = "EXPERIENCIA", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(
                    text = "%d / %d XP".format(xp, maxXp),
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { if (maxXp > 0) xp.toFloat() / maxXp.toFloat() else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = CyanAccent,
                trackColor = Color(0xFF2A2A2A)
            )
        }
    }
}

@Composable
fun Badge(text: String, textColor: Color, borderColor: Color) {
    Box(
        modifier = Modifier
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .background(borderColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatSquareCard(modifier: Modifier = Modifier, title: String, value: String, unit: String, valueColor: Color) {
    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = title, color = DarkGrayText, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = value, color = valueColor, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                if (unit.isNotEmpty()) {
                    Text(text = unit, color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp))
                }
            }
        }
    }
}

@Composable
fun DuelsCard(won: String, total: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "DUELOS GANADOS", color = DarkGrayText, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = won, color = NeonGreen, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(text = " de $total disputados", color = TextGray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 6.dp, start = 8.dp))
            }
        }
    }
}

@Composable
fun WorkoutActionCard(icon: ImageVector, title: String, subtitle: String, details: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardDarkBackground),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222222)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, color = White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = " • $subtitle", color = White, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = details, color = TextGray, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .background(NeonGreen, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("GO", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun CustomBottomNavigation(
    onNavigateToSocial: () -> Unit,
    onNavigateToDuel: () -> Unit,
    hasPendingRequests: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(vertical = 12.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(icon = Icons.Default.Bolt, text = "Inicio", color = NeonGreen, isSelected = true) {}
        BottomNavItem(icon = Icons.Default.SportsMma, text = "Duelo", color = Color(0xFFFF5252), isSelected = false) {
            onNavigateToDuel()
        }
        BottomNavItem(icon = Icons.Default.EmojiEvents, text = "Ranking", color = Color(0xFFFFB300), isSelected = false) {}
        BottomNavItem(icon = Icons.Default.Group, text = "Amigos", color = Color(0xFFB388FF), isSelected = false, hasBadge = hasPendingRequests) {
            onNavigateToSocial()
        }
    }
}

@Composable
fun BottomNavItem(icon: ImageVector, text: String, color: Color, isSelected: Boolean, hasBadge: Boolean = false, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = if (isSelected) color else TextGray,
                modifier = Modifier.size(28.dp)
            )
            if (hasBadge) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = if (isSelected) color else TextGray,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}