package com.margaritaolivera.atleta.features.auth.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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

@Composable
fun RegisterScreen(
    state: AuthUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    onResetState: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            android.widget.Toast.makeText(context, "Registro exitoso", android.widget.Toast.LENGTH_SHORT).show()
            onResetState()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(SurfaceDark, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "CREAR CUENTA",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = White,
            letterSpacing = 2.sp
        )

        Text(
            text = "Únete a la comunidad de atletas",
            color = TextGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(56.dp))

        Text(
            "NOMBRE",
            modifier = Modifier.fillMaxWidth(),
            color = TextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.nameRegister,
            onValueChange = onNameChange,
            placeholder = "Nombre completo",
            icon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "CORREO",
            modifier = Modifier.fillMaxWidth(),
            color = TextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.emailRegister,
            onValueChange = onEmailChange,
            placeholder = "tu@correo.com",
            icon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "CONTRASEÑA",
            modifier = Modifier.fillMaxWidth(),
            color = TextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = state.passwordRegister,
            onValueChange = onPasswordChange,
            placeholder = "••••••••",
            icon = Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
            shape = RoundedCornerShape(20.dp),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.Black)
            } else {
                Text(
                    "REGISTRARME",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }

        TextButton(onClick = onBackToLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = White)
        }

        state.error?.let { errorMessage ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
    }
}