package com.margaritaolivera.atleta.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.margaritaolivera.atleta.core.navigation.Screens
import com.margaritaolivera.atleta.core.ui.theme.DarkBackground
import com.margaritaolivera.atleta.core.ui.theme.NeonGreen
import com.margaritaolivera.atleta.core.ui.theme.TextGray

@Composable
fun AtletaBottomBar(
    currentRoute: NavDestination?,
    onNavigateToHome: () -> Unit,
    onNavigateToDuel: () -> Unit,
    onNavigateToRanking: () -> Unit,
    onNavigateToSocial: () -> Unit,
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
        BottomNavItem(
            icon = Icons.Default.Bolt,
            text = "Inicio",
            color = NeonGreen,
            isSelected = currentRoute?.hierarchy?.any { it.hasRoute<Screens.Home>() } == true,
            onClick = onNavigateToHome
        )
        BottomNavItem(
            icon = Icons.Default.SportsMma,
            text = "Duelo",
            color = Color(0xFFFF5252),
            isSelected = currentRoute?.hierarchy?.any { it.hasRoute<Screens.DuelSelection>() } == true,
            onClick = onNavigateToDuel
        )
        BottomNavItem(
            icon = Icons.Default.EmojiEvents,
            text = "Ranking",
            color = Color(0xFFFFB300),
            isSelected = currentRoute?.hierarchy?.any { it.hasRoute<Screens.Ranking>() } == true,
            onClick = onNavigateToRanking
        )
        BottomNavItem(
            icon = Icons.Default.Group,
            text = "Amigos",
            color = Color(0xFFB388FF),
            isSelected = currentRoute?.hierarchy?.any { it.hasRoute<Screens.Social>() } == true,
            hasBadge = hasPendingRequests,
            onClick = onNavigateToSocial
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    text: String,
    color: Color,
    isSelected: Boolean,
    hasBadge: Boolean = false,
    onClick: () -> Unit
) {
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