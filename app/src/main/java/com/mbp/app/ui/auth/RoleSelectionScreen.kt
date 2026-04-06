package com.mbp.app.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbp.app.ui.theme.MBPBlue
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun RoleSelectionScreen(
    onSelectCustomer: () -> Unit,
    onSelectStakeholder: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MBPDark)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MBPBrand()
            Spacer(Modifier.height(48.dp))
            Text(
                text = "Choose how to continue",
                color = MBPGray,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(32.dp))
            RoleCard(
                title = "I am a Customer",
                subtitle = "View your unit status",
                icon = Icons.Filled.Home,
                accent = MBPGold,
                onClick = onSelectCustomer
            )
            Spacer(Modifier.height(20.dp))
            RoleCard(
                title = "I am a Stakeholder",
                subtitle = "View project overview",
                icon = Icons.Filled.BarChart,
                accent = MBPBlue,
                onClick = onSelectStakeholder
            )
        }
    }
}

@Composable
fun MBPBrand() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MBPGold),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "MB",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            "Mosul Boulevard",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
    }
}

@Composable
private fun RoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
        border = BorderStroke(1.5.dp, accent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(20.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(subtitle, color = MBPGray, fontSize = 13.sp)
            }
        }
    }
}
