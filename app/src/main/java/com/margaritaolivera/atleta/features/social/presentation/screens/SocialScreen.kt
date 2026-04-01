package com.margaritaolivera.atleta.features.social.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.SocialViewModel

@Composable
fun SocialScreen(viewModel: SocialViewModel) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("AMIGOS", "PENDIENTES", "RANKING")

    Column(modifier = Modifier.fillMaxSize().background(DarkBackground)) {
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
                    text = { Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = NeonGreen) }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedTab) {
                    0 -> items(state.friends) { friend -> AthleteItem(friend.nombre, friend.nivel) }
                    1 -> items(state.pendingRequests) { req ->
                        PendingItem(req.nombre) { viewModel.acceptFriend(req.id) }
                    }
                    2 -> items(state.ranking) { rank ->
                        RankingItem(rank.posicion, rank.nombre, rank.nivel, rank.experiencia)
                    }
                }
            }
        }
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

@Composable
fun RankingItem(pos: Int, nombre: String, nivel: Int, xp: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#$pos", color = if(pos <= 3) NeonGreen else TextGray, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, modifier = Modifier.width(40.dp))
        AthleteItem(nombre, nivel) // Reutilizamos el item
    }
}