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
import com.mbp.app.data.model.SalesSummaryResponse
import com.mbp.app.data.model.StatusReportResponse
import com.mbp.app.data.model.TowerSalesRow
import com.mbp.app.data.model.TowerStatusRow
import com.mbp.app.data.model.VillaSalesRow
import com.mbp.app.data.model.VillaStatusRow
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
                0 -> SalesContent(state.sales)
                1 -> StatusContent(state.structural)
                2 -> StatusContent(state.finishing)
                3 -> StatusContent(state.facade)
            }
        }
    }
}

@Composable
private fun SalesContent(sales: SalesSummaryResponse?) {
    if (sales == null) {
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
        if (sales.villas.isNotEmpty()) {
            item(key = "h_villas") {
                Text("Villas", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            items(sales.villas, key = { "vs_${it.villaTypeId ?: it.villaTypeName}" }) {
                VillaSalesCard(it)
            }
        }
        if (sales.towers.isNotEmpty()) {
            item(key = "h_towers") {
                Spacer(Modifier.height(4.dp))
                Text("Towers", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            items(sales.towers, key = { "ts_${it.towerDefinitionId ?: it.towerName}" }) {
                TowerSalesCard(it)
            }
        }
        if (sales.villas.isEmpty() && sales.towers.isEmpty()) {
            item { Text("No data", color = MBPGray) }
        }
    }
}

@Composable
private fun VillaSalesCard(row: VillaSalesRow) {
    SalesRowCard(
        label = row.villaTypeName ?: "Villa",
        sold = row.totalSold,
        unsold = row.totalUnsold,
        total = row.total
    )
}

@Composable
private fun TowerSalesCard(row: TowerSalesRow) {
    SalesRowCard(
        label = row.towerName ?: "Tower",
        sold = row.totalSold,
        unsold = row.totalUnsold,
        total = row.total
    )
}

@Composable
private fun SalesRowCard(label: String, sold: Int, unsold: Int, total: Int) {
    val pct = if (total > 0) sold.toFloat() / total else 0f
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Row {
                Text("Sold $sold", color = MBPSuccess, fontSize = 13.sp,
                    modifier = Modifier.weight(1f))
                Text("Available $unsold", color = MBPGold, fontSize = 13.sp)
                Spacer(Modifier.padding(horizontal = 6.dp))
                Text("Total $total", color = MBPGray, fontSize = 13.sp)
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
        if (report.villas.isNotEmpty()) {
            item(key = "h_villas") {
                Text("Villas", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            val total = report.villas.sumOf { it.total }
            items(report.villas, key = { "vsr_${it.statusName}" }) {
                StatusRowCard(it.statusName, it.total, total)
            }
        }
        if (report.towers.isNotEmpty()) {
            item(key = "h_towers") {
                Spacer(Modifier.height(8.dp))
                Text("Towers", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            val total = report.towers.sumOf { it.total }
            items(report.towers, key = { "tsr_${it.statusName}" }) {
                StatusRowCard(it.statusName, it.total, total)
            }
        }
        if (report.villas.isEmpty() && report.towers.isEmpty()) {
            item { Text("No data", color = MBPGray) }
        }
    }
}

@Composable
private fun StatusRowCard(name: String?, count: Int, total: Int) {
    val pct = if (total > 0) count.toFloat() / total else 0f
    Card(
        Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row {
                Text(
                    name ?: "—",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(count.toString(), color = MBPGold, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { pct.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MBPGold,
                trackColor = MBPDark,
            )
        }
    }
}
