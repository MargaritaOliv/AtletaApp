package com.margaritaolivera.atleta.features.training.presentation.screens

import com.airbnb.lottie.compose.*
import com.margaritaolivera.atleta.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toArgb
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.training.presentation.viewmodels.WorkoutSessionViewModel

@Composable
fun WorkoutSessionScreen(
    viewModel: WorkoutSessionViewModel,
    workoutType: String,
    opponentName: String? = null,
    opponentXp: Int = 0,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.prepareWorkout(workoutType, opponentName, opponentXp)
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
            .padding(24.dp)
    ) {
        if (state.isLoading && !state.isTracking) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = NeonGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Preparando entrenamiento...", color = TextGray)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(SurfaceDark, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = White)
                }

                Text(
                    text = workoutType.uppercase(),
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )

                Box(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (state.isDuel) {
                DuelHeader(
                    opponentName = state.opponentName ?: "Oponente",
                    xpRemaining = state.initialXpGap
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricBox(value = formatTime(state.timerSeconds), label = "TIEMPO")
                val goalText = if (workoutType == "SQUAT") "${state.goalValue}" else "${state.goalValue}m"
                MetricBox(value = goalText, label = "OBJETIVO")
            }

            val animationResId = when (workoutType) {
                "SQUAT" -> R.raw.anim_squat
                "RUN" -> R.raw.anim_run
                "JOG" -> R.raw.anim_jog
                else -> null
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (animationResId != null) {
                    // Cargar la animación local del archivo .json
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationResId))
                    
                    // Manejar el progreso: Correr toda la vida si está trackeando (entrenando)
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        isPlaying = state.isTracking
                    )

                    // Filtro para cambiar TODO el color de la animación al verde neón de la app
                    val dynamicProperties = rememberLottieDynamicProperties(
                        rememberLottieDynamicProperty(
                            property = LottieProperty.COLOR_FILTER,
                            value = SimpleColorFilter(NeonGreen.toArgb()),
                            keyPath = arrayOf("**")
                        )
                    )

                Box(
                    modifier = Modifier
                        .fillMaxSize(0.8f) // Mismo tamaño que la animación original
                        .align(Alignment.Center)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        dynamicProperties = dynamicProperties,
                        modifier = Modifier.fillMaxSize() // Ahora llena el contenedor (que ya tiene el 0.8f)
                    )

                    // El 'Parche' para tapar la marca de agua: Un box del mismo color del fondo
                    Box(
                        modifier = Modifier
                            .size(75.dp, 25.dp) // Tamaño aproximado de la píldora de SVGator
                            .align(Alignment.BottomEnd) // Posición abajo a la derecha
                            .offset(y = (-5).dp) // Pequeño ajuste para centrarlo sobre el logo
                            .background(DarkBackground)
                    )
                }
                } else {
                    Text(
                        text = "[ Espacio para Animación ]",
                        color = TextGray.copy(alpha = 0.2f),
                        fontSize = 14.sp
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val mainValue = if (workoutType == "SQUAT") "${state.currentReps}" else "%.2f".format(state.distanceKm)
                val unitLabel = if (workoutType == "SQUAT") "REPETICIONES" else "KILÓMETROS"
                
                Text(
                    text = mainValue,
                    fontSize = 80.sp,
                    color = White,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 80.sp
                )
                Text(
                    text = unitLabel,
                    fontSize = 14.sp,
                    color = NeonGreen,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(NeonGreen, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Total guardado: ", color = TextGray, fontSize = 14.sp)
                val savedText = if (state.isStarted) {
                    val totalAccumulatedReps = state.totalReps + state.currentReps
                    if (workoutType == "SQUAT") "$totalAccumulatedReps reps"
                    else "%.2f km".format(state.distanceKm)
                } else {
                    if (workoutType == "SQUAT") {
                        if (state.previousTotal > 0) "${state.previousTotal.toInt()} reps" else "-- reps"
                    } else {
                        if (state.previousTotal > 0) "%.2f km".format(state.previousTotal) else "-- km"
                    }
                }
                Text(text = savedText, color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!state.isStarted) {
                Button(
                    onClick = { viewModel.startTracking() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("INICIAR ENTRENAMIENTO", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (workoutType == "SQUAT") {
                        Button(
                            onClick = { viewModel.saveSet() },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                                .border(1.dp, SurfaceDark, RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("GUARDAR SERIE", color = White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = { viewModel.finishWorkout() },
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (workoutType == "SQUAT") Color(0xFFFF5252) else NeonGreen),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("FINALIZAR", color = if (workoutType == "SQUAT") White else Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DuelHeader(opponentName: String, xpRemaining: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, if (xpRemaining > 0) Color(0xFFFF5252) else NeonGreen, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = if (xpRemaining > 0) Color(0xFFFF5252).copy(0.1f) else NeonGreen.copy(0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).background(if (xpRemaining > 0) Color(0xFFFF5252) else NeonGreen, CircleShape), Alignment.Center) {
                Icon(if (xpRemaining > 0) Icons.Default.SportsMma else Icons.Default.EmojiEvents, null, 
                    tint = if (xpRemaining > 0) White else Color.Black, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column {
                if (xpRemaining > 0) {
                    Text("RETO: SUPERAR A", color = Color(0xFFFF5252), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    Text(opponentName.uppercase(), color = White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    Text("Faltan $xpRemaining XP para ganar", color = White.copy(0.8f), fontSize = 11.sp)
                } else {
                    Text("¡POSICIÓN GANADA!", color = NeonGreen, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                    Text("HAS SUPERADO A $opponentName", color = White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

private fun formatTime(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}

@Composable
fun MetricBox(value: String, label: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                color = TextGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}