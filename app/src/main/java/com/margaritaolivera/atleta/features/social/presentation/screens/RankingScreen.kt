package com.margaritaolivera.atleta.features.social.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.margaritaolivera.atleta.core.ui.theme.*
import com.margaritaolivera.atleta.features.social.domain.entities.RankingAthlete
import com.margaritaolivera.atleta.features.social.presentation.viewmodels.SocialViewModel

val Gold = Color(0xFFFFD700)
val Silver = Color(0xFFC0C0C0)
val Bronze = Color(0xFFCD7F32)

@Composable
fun RankingScreen(viewModel: SocialViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Cabecera
        Text(
            text = "Clasificación Global",
            color = White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, bottom = 16.dp)
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NeonGreen)
            }
        } else if (state.ranking.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay datos de ranking aún", color = TextGray)
            }
        } else {
            // Separamos el Top 3 del resto
            val top3 = state.ranking.take(3)
            val restOfRanking = state.ranking.drop(3)

            // Podio
            if (top3.isNotEmpty()) {
                PodiumSection(top3 = top3)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista del 4 en adelante
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(restOfRanking) { athlete ->
                    RankingListItem(athlete = athlete)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun PodiumSection(top3: List<RankingAthlete>) {
    val first = top3.getOrNull(0)
    val second = top3.getOrNull(1)
    val third = top3.getOrNull(2)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .height(220.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // Segundo Lugar
        if (second != null) {
            PodiumPillar(athlete = second, rank = 2, height = 130, color = Silver)
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Primer Lugar
        if (first != null) {
            PodiumPillar(athlete = first, rank = 1, height = 170, color = Gold, isFirst = true)
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Tercer Lugar
        if (third != null) {
            PodiumPillar(athlete = third, rank = 3, height = 100, color = Bronze)
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RowScope.PodiumPillar(athlete: RankingAthlete, rank: Int, height: Int, color: Color, isFirst: Boolean = false) {
    val initials = athlete.nombre.take(2).uppercase()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        if (isFirst) {
            Icon(Icons.Default.Star, contentDescription = "Oro", tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Avatar
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color.copy(alpha = 0.2f), CircleShape)
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = initials, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

            // Badge de número de ranking
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color, CircleShape)
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$rank", color = DarkBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = athlete.nombre.substringBefore(" "), color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = "${athlete.experiencia} pts", color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Pilar del podio
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(height.dp)
                .background(
                    color = SurfaceDark,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .border(
                    width = 1.dp,
                    color = color.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(color)
            )
        }
    }
}

@Composable
fun RankingListItem(athlete: RankingAthlete) {
    val initials = athlete.nombre.take(2).uppercase()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${athlete.posicion}",
                color = TextGray,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                modifier = Modifier.width(30.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(text = initials, color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = athlete.nombre, color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Nivel ${athlete.nivel}", color = TextGray, fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${athlete.experiencia}", color = NeonGreen, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text(text = "pts", color = TextGray, fontSize = 12.sp)
            }
        }
    }
}