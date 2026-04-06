package com.mbp.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPError
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun PhoneLoginScreen(
    onBack: () -> Unit,
    onLoggedIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(state.isLoggedIn, state.userRole) {
        if (state.isLoggedIn && state.userRole == "customer") onLoggedIn()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MBPDark)
            .padding(24.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MBPGold),
                contentAlignment = Alignment.Center
            ) {
                Text("MB", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Customer Login",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Enter your phone number to view your units",
                color = MBPGray,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Phone number") },
                placeholder = { Text("+964 ...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = mbpFieldColors(MBPGold)
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { viewModel.loginWithPhone(phone.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = phone.isNotBlank() && !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MBPGold,
                    contentColor = Color.White
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Sign In", fontWeight = FontWeight.SemiBold)
                }
            }
            if (state.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(state.error ?: "", color = MBPError, fontSize = 13.sp)
            }
        }
    }
}

@Composable
internal fun mbpFieldColors(accent: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = accent,
    unfocusedBorderColor = MBPGray.copy(alpha = 0.5f),
    focusedLabelColor = accent,
    unfocusedLabelColor = MBPGray,
    cursorColor = accent,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = MBPSurface,
    unfocusedContainerColor = MBPSurface,
)
