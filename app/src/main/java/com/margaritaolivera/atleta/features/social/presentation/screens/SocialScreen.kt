package com.margaritaolivera.atleta.features.social.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.NeonGreen
import com.margaritaolivera.atleta.core.ui.theme.SurfaceDark
import com.margaritaolivera.atleta.core.ui.theme.TextGray
import com.margaritaolivera.atleta.core.ui.theme.White
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.SocialViewModel

@Composable
fun SocialScreen(viewModel: SocialViewModel) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("AMIGOS", "PENDIENTES")

    var showAddFriendDialog by remember { mutableStateOf(false) }
    var friendUidInput by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddFriendDialog = true },
                    containerColor = NeonGreen,
                    contentColor = DarkBackground,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir amigo")
                }
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SurfaceDark,
                contentColor = NeonGreen,
                indicator = { TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(it[selectedTab]), color = NeonGreen) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                if (index == 1 && state.pendingRequests.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .background(Color.Red, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = state.pendingRequests.size.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = NeonGreen) }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    when (selectedTab) {
                        0 -> {
                            if (state.friends.isEmpty()) {
                                item { EmptyStateText("Aún no tienes amigos agregados") }
                            } else {
                                items(state.friends) { friend -> AthleteItem(friend.nombre, friend.nivel) }
                            }
                        }
                        1 -> {
                            if (state.pendingRequests.isEmpty()) {
                                item { EmptyStateText("No tienes solicitudes pendientes") }
                            } else {
                                items(state.pendingRequests) { req ->
                                    PendingItem(req.nombre) { viewModel.acceptFriend(req.id) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddFriendDialog) {
        AlertDialog(
            onDismissRequest = { showAddFriendDialog = false },
            containerColor = SurfaceDark,
            title = {
                Text("Agregar Amigo", color = White, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text("Ingresa el correo de la persona que deseas agregar:", color = TextGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = friendUidInput,
                        onValueChange = { friendUidInput = it },
                        placeholder = { Text("Ej: amigo@gmail.com", color = TextGray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = TextGray
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (friendUidInput.isNotBlank()) {
                            viewModel.sendRequest(friendUidInput)
                            friendUidInput = ""
                            showAddFriendDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Text("ENVIAR", color = DarkBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFriendDialog = false }) {
                    Text("CANCELAR", color = TextGray)
                }
            }
        )
    }
}

@Composable
fun EmptyStateText(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = TextGray, fontSize = 14.sp)
    }
}

@Composable
fun AthleteItem(nombre: String, nivel: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(NeonGreen, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = DarkBackground)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(nombre, color = White, fontWeight = FontWeight.Bold)
                Text("NIVEL $nivel", color = NeonGreen, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PendingItem(nombre: String, onAccept: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(nombre, color = White, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            IconButton(onClick = onAccept, modifier = Modifier.background(NeonGreen, CircleShape).size(32.dp)) {
                Icon(Icons.Default.Check, null, tint = DarkBackground, modifier = Modifier.size(20.dp))
            }
        }
    }
}