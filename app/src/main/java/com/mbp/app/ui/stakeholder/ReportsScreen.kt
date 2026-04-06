package com.mbp.app.ui.stakeholder

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mbp.app.data.model.SalesGroup
import com.mbp.app.data.model.StatusBucket
import com.mbp.app.data.model.StatusReportResponse
import com.mbp.app.ui.customer.parseColor
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPError
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSuccess
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selected by remember { mutableIntStateOf(0) }
    val tabs = listOf("Sales", "Structural", "Finishing", "Facade")

    Column(
        Modifier
            .fillMaxSize()
            .background(MBPDark)
    ) {
        Text(
            "Reports",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        TabRow(
            selectedTabIndex = selected,
            containerColor = MBPDark,
            contentColor = MBPGold,
        ) {
            tabs.forEachIndexed { i, t ->
                Tab(
                    selected = selected == i,
                    onClick = { selected = i },
                    text = { Text(t, color = if (selected == i) MBPGold else MBPGray) }
                )
            }
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MBPGold)
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = MBPError)
            }
            else -> when (selected) {
                0 -> SalesContent(state.sales?.villas, state.sales?.towerUnits)
                1 -> StatusContent(state.structural)
                2 -> StatusContent(state.finishing)
                3 -> StatusContent(state.facade)
            }
        }
    }
}

@Composable
private fun SalesContent(villas: SalesGroup?, units: SalesGroup?) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (villas != null) item { SalesCard("Villas", villas) }
        if (units != null) item { SalesCard("Tower Units", units) }
        if (villas == null && units == null) {
            item { Text("No data", color = MBPGray) }
        }
    }
}

@Composable
private fun SalesCard(label: String, group: SalesGroup) {
    val pct = if (group.total > 0) group.sold.toFloat() / group.total else 0f
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Row {
                Text("Sold ${group.sold}", color = MBPSuccess, fontSize = 13.sp,
                    modifier = Modifier.weight(1f))
                Text("Available ${group.available}", color = MBPGold, fontSize = 13.sp)
                Spacer(Modifier.padding(horizontal = 6.dp))
                Text("Total ${group.total}", color = MBPGray, fontSize = 13.sp)
            }
            Spacer(Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { pct.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MBPSuccess,
                trackColor = MBPDark,
            )
        }
    }
}

@Composable
private fun StatusContent(report: StatusReportResponse?) {
    if (report == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data", color = MBPGray)
        }
        return
    }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("Villas", color = Color.White, fontWeight = FontWeight.SemiBold) }
        items(report.villas) { b -> StatusBucketCard(b, report.villas.sumOf { it.count }) }
        item { Spacer(Modifier.height(8.dp)) }
        item { Text("Tower Units", color = Color.White, fontWeight = FontWeight.SemiBold) }
        items(report.towerUnits) { b -> StatusBucketCard(b, report.towerUnits.sumOf { it.count }) }
    }
}

@Composable
private fun StatusBucketCard(bucket: StatusBucket, total: Int) {
    val color = parseColor(bucket.status?.colorCode) ?: MBPGold
    val pct = if (total > 0) bucket.count.toFloat() / total else 0f
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row {
                Text(
                    bucket.status?.name ?: "—",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(bucket.count.toString(), color = color, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { pct.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = MBPDark,
            )
        }
    }
}
