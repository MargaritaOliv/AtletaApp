package com.margaritaolivera.atleta.features.social.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.DuelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuelSelectionScreen(
    viewModel: DuelViewModel,
    onNavigateToWorkout: (String, String, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedFriendForWorkout by remember { mutableStateOf<String?>(null) }
    var selectedFriendXp by remember { mutableIntStateOf(0) }
    var showWorkoutSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CENTRO DE DUELOS", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Atrás", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 20.dp)) {
            
            Text(
                "RETAR A UN AMIGO",
                color = TextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = NeonGreen) }
            } else if (state.friends.isEmpty()) {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("No tienes amigos para retar", color = TextGray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val sortedFriends = state.friends.sortedByDescending { it.experiencia }
                    items(sortedFriends) { friend ->
                        val xpGap = friend.experiencia - state.userXp
                        val isAhead = xpGap > 0
                        
                        DuelOpponentItem(
                            name = friend.nombre,
                            level = friend.nivel,
                            xpGap = if (isAhead) xpGap else 0,
                            onChallenge = {
                                selectedFriendForWorkout = friend.nombre
                                selectedFriendXp = friend.experiencia
                                showWorkoutSelector = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showWorkoutSelector) {
        AlertDialog(
            onDismissRequest = { showWorkoutSelector = false },
            containerColor = SurfaceDark,
            title = { Text("ELIGE TU EJERCICIO", color = White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    WorkoutOption("SENTADILLAS", "SQUAT") {
                        showWorkoutSelector = false
                        onNavigateToWorkout("SQUAT", selectedFriendForWorkout!!, selectedFriendXp)
                    }
                    WorkoutOption("CARRERA", "RUN") {
                        showWorkoutSelector = false
                        onNavigateToWorkout("RUN", selectedFriendForWorkout!!, selectedFriendXp)
                    }
                    WorkoutOption("TROTE", "JOG") {
                        showWorkoutSelector = false
                        onNavigateToWorkout("JOG", selectedFriendForWorkout!!, selectedFriendXp)
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun DuelOpponentItem(name: String, level: Int, xpGap: Int, onChallenge: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(45.dp).background(if (xpGap > 0) Color(0xFFFF5252).copy(0.1f) else NeonGreen.copy(0.1f), CircleShape), Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = if (xpGap > 0) Color(0xFFFF5252) else NeonGreen)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(name, color = White, fontWeight = FontWeight.Bold)
                if (xpGap > 0) {
                    Text("A $xpGap XP de superarlo", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("¡Ya lo has superado!", color = NeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = onChallenge,
                colors = ButtonDefaults.buttonColors(containerColor = if (xpGap > 0) Color(0xFFFF5252) else NeonGreen),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                modifier = Modifier.height(35.dp)
            ) {
                Text(if (xpGap > 0) "RETAR" else "ENTRENAR", color = if (xpGap > 0) White else Color.Black, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun WorkoutOption(title: String, type: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color(0xFF222222),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (type == "SQUAT") Icons.Default.FitnessCenter else Icons.Default.LocalFireDepartment, 
                null, 
                tint = NeonGreen
            )
            Spacer(Modifier.width(16.dp))
            Text(title, color = White, fontWeight = FontWeight.Bold)
        }
    }
}
