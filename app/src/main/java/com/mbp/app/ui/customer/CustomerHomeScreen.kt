package com.mbp.app.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import com.mbp.app.ui.theme.MBPBlue
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun CustomerHomeScreen(
    onOpenVilla: (Int) -> Unit,
    onOpenTowerUnit: (Int) -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: CustomerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val customer = state.customer

    Column(
        Modifier
            .fillMaxSize()
            .background(MBPDark)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("Welcome", color = MBPGray, fontSize = 12.sp)
                Text(
                    customer?.name ?: "Customer",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            IconButton(onClick = { viewModel.logout(onLoggedOut) }) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = MBPGold
                )
            }
        }

        val villas = customer?.villas.orEmpty()
        val units = customer?.towerUnits.orEmpty()

        if (villas.isEmpty() && units.isEmpty() && !state.isRefreshing) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No units linked to your account",
                    color = MBPGray
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (villas.isNotEmpty()) {
                item(key = "header_villas") {
                    SectionHeader("Your Villas", MBPGold)
                }
                items(villas, key = { "villa_${it.id}" }) { v ->
                    VillaCard(v) { onOpenVilla(v.id) }
                }
            }
            if (units.isNotEmpty()) {
                item(key = "header_units") {
                    Spacer(Modifier.height(8.dp))
                    SectionHeader("Your Apartments", MBPBlue)
                }
                items(units, key = { "unit_${it.id}" }) { u ->
                    TowerUnitCard(u) { onOpenTowerUnit(u.id) }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun VillaCard(villa: VillaDto, onClick: () -> Unit) {
    UnitCardLayout(
        accent = MBPGold,
        code = villa.code,
        subtitle = villa.villaType?.name ?: "Villa",
        completion = villa.completionPct ?: 0,
        stage = villa.currentStage?.name,
        status = villa.status,
        onClick = onClick,
    )
}

@Composable
private fun TowerUnitCard(unit: TowerUnitDto, onClick: () -> Unit) {
    val sub = listOfNotNull(
        unit.towerDefinition?.name,
        unit.floorDefinition?.name?.let { "Floor $it" }
    ).joinToString(" • ").ifBlank { "Apartment" }
    UnitCardLayout(
        accent = MBPBlue,
        code = unit.code,
        subtitle = sub,
        completion = unit.completionPct ?: 0,
        stage = unit.currentStage?.name,
        status = unit.status,
        onClick = onClick,
    )
}

@Composable
private fun UnitCardLayout(
    accent: Color,
    code: String,
    subtitle: String,
    completion: Int,
    stage: String?,
    status: com.mbp.app.data.model.StatusDto?,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    code,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                if (status != null) StatusBadge(status)
            }
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = MBPGray, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { (completion / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = accent,
                    trackColor = MBPDark,
                )
                Spacer(Modifier.size(8.dp))
                Text("$completion%", color = Color.White, fontSize = 13.sp)
            }
            if (!stage.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text("Stage: $stage", color = MBPGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StatusBadge(status: com.mbp.app.data.model.StatusDto) {
    val color = parseColor(status.colorCode) ?: MBPGold
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.18f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.size(6.dp))
        Text(status.name, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

fun parseColor(hex: String?): Color? {
    if (hex.isNullOrBlank()) return null
    return runCatching {
        val clean = hex.removePrefix("#")
        val v = clean.toLong(16)
        if (clean.length == 6) Color(0xFF000000 or v) else Color(v)
    }.getOrNull()
}
