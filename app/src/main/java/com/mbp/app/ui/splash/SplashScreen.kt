package com.mbp.app.ui.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1000)
        onTimeout()
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(MBPDark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MBPGold)
                    .border(BorderStroke(2.dp, MBPGold), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "MB",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Mosul Boulevard",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )
            Spacer(Modifier.height(6.dp))
            Text("Premium Real Estate", color = MBPGray, fontSize = 14.sp)
        }
    }
}
