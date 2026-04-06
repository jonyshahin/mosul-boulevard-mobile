package com.mbp.app.ui.customer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mbp.app.data.model.StatusDto
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import com.mbp.app.ui.theme.MBPBlue
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPError
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSuccess
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun UnitDetailScreen(
    unitId: Int,
    unitType: String,
    onBack: () -> Unit,
    onOpenSiteUpdates: (Int, String) -> Unit,
    viewModel: UnitDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(unitId, unitType) { viewModel.load(unitId, unitType) }

    val accent = if (unitType == "villa") MBPGold else MBPBlue

    Column(
        Modifier
            .fillMaxSize()
            .background(MBPDark)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text("Unit Detail", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accent)
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = MBPError)
            }
            state.villa != null -> VillaContent(state.villa!!, accent) {
                onOpenSiteUpdates(unitId, unitType)
            }
            state.towerUnit != null -> TowerContent(state.towerUnit!!, accent) {
                onOpenSiteUpdates(unitId, unitType)
            }
        }
    }
}

@Composable
private fun VillaContent(v: VillaDto, accent: Color, onSiteUpdates: () -> Unit) {
    DetailBody(
        accent = accent,
        code = v.code,
        subtitle = v.villaType?.name ?: "Villa",
        isSold = v.isSold,
        completionPct = v.completionPct ?: 0,
        overallStatus = v.status,
        structuralStatus = v.structuralStatus,
        finishingStatus = v.finishingStatus,
        facadeStatus = v.facadeStatus,
        stage = v.currentStage?.name,
        engineer = v.engineer?.name,
        plannedStart = v.plannedStartDate,
        plannedFinish = v.plannedFinishDate,
        actualStart = v.actualStartDate,
        actualFinish = v.actualFinishDate,
        onSiteUpdates = onSiteUpdates,
    )
}

@Composable
private fun TowerContent(u: TowerUnitDto, accent: Color, onSiteUpdates: () -> Unit) {
    val sub = listOfNotNull(
        u.towerDefinition?.name,
        u.floorDefinition?.name?.let { "Floor $it" }
    ).joinToString(" • ").ifBlank { "Apartment" }
    DetailBody(
        accent = accent,
        code = u.code,
        subtitle = sub,
        isSold = u.isSold,
        completionPct = u.completionPct ?: 0,
        overallStatus = u.status,
        structuralStatus = u.structuralStatus,
        finishingStatus = u.finishingStatus,
        facadeStatus = u.facadeStatus,
        stage = u.currentStage?.name,
        engineer = u.engineer?.name,
        plannedStart = u.plannedStartDate,
        plannedFinish = u.plannedFinishDate,
        actualStart = u.actualStartDate,
        actualFinish = u.actualFinishDate,
        onSiteUpdates = onSiteUpdates,
    )
}

@Composable
private fun DetailBody(
    accent: Color,
    code: String,
    subtitle: String,
    isSold: Boolean,
    completionPct: Int,
    overallStatus: StatusDto?,
    structuralStatus: StatusDto?,
    finishingStatus: StatusDto?,
    facadeStatus: StatusDto?,
    stage: String?,
    engineer: String?,
    plannedStart: String?,
    plannedFinish: String?,
    actualStart: String?,
    actualFinish: String?,
    onSiteUpdates: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(PaddingValues(16.dp)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MBPSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(code, color = accent, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text(subtitle, color = MBPGray, fontSize = 13.sp)
                }
                if (isSold) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MBPSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("SOLD", color = MBPSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Completion circle
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MBPSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Completion", color = MBPGray, fontSize = 13.sp)
                Spacer(Modifier.height(12.dp))
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { (completionPct / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.size(120.dp),
                        color = accent,
                        strokeWidth = 10.dp,
                        trackColor = MBPDark,
                    )
                    Text(
                        "$completionPct%",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 2x2 status grid
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusTile("Overall", overallStatus, Modifier.weight(1f))
                StatusTile("Structural", structuralStatus, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusTile("Finishing", finishingStatus, Modifier.weight(1f))
                StatusTile("Facade", facadeStatus, Modifier.weight(1f))
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MBPSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailRow("Stage", stage)
                DetailRow("Engineer", engineer)
                DetailRow("Planned start", plannedStart)
                DetailRow("Planned finish", plannedFinish)
                DetailRow("Actual start", actualStart)
                DetailRow("Actual finish", actualFinish)
            }
        }

        Button(
            onClick = onSiteUpdates,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.White)
        ) {
            Text("View Site Updates", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun StatusTile(label: String, status: StatusDto?, modifier: Modifier = Modifier) {
    val color = parseColor(status?.colorCode) ?: MBPGray
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(label, color = MBPGray, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    status?.name ?: "—",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String?) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, color = MBPGray, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value ?: "—", color = Color.White, fontSize = 13.sp)
    }
}
