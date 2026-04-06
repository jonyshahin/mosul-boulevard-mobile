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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.mbp.app.ui.auth.mbpFieldColors
import com.mbp.app.ui.customer.StatusBadge
import com.mbp.app.ui.theme.MBPBlue
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSuccess
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun VillasListScreen(
    onOpenVilla: (Int) -> Unit,
    viewModel: VillasListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(MBPDark)
    ) {
        Text(
            "Villas",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = state.search,
            onValueChange = viewModel::setSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            label = { Text("Search") },
            singleLine = true,
            colors = mbpFieldColors(MBPGold),
        )
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipMBP("All", state.villaTypeId == null) { viewModel.setType(null) }
            FilterChipMBP("Type A", state.villaTypeId == 1) { viewModel.setType(1) }
            FilterChipMBP("Type B", state.villaTypeId == 2) { viewModel.setType(2) }
        }
        Spacer(Modifier.height(8.dp))

        val listState = rememberLazyListState()
        val shouldLoadMore by remember {
            derivedStateOf {
                val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                last >= state.items.size - 3 && state.canLoadMore && !state.isLoading
            }
        }
        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore) viewModel.loadMore()
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.items, key = { it.id }) { v -> VillaRow(v) { onOpenVilla(v.id) } }
            if (state.isLoading) {
                item {
                    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MBPGold)
                    }
                }
            }
        }
    }
}

@Composable
internal fun FilterChipMBP(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MBPSurface,
            labelColor = MBPGray,
            selectedContainerColor = MBPGold.copy(alpha = 0.2f),
            selectedLabelColor = MBPGold,
        )
    )
}

@Composable
internal fun VillaRow(v: VillaDto, onClick: () -> Unit) {
    UnitRow(
        accent = MBPGold,
        code = v.code,
        subtitle = v.villaType?.name ?: "Villa",
        completion = v.completionPct ?: 0,
        stage = v.currentStage?.name,
        status = v.status,
        isSold = v.isSold,
        onClick = onClick
    )
}

@Composable
internal fun TowerRow(u: TowerUnitDto, onClick: () -> Unit) {
    val sub = listOfNotNull(
        u.towerDefinition?.name,
        u.floorDefinition?.name?.let { "Floor $it" }
    ).joinToString(" • ").ifBlank { "Apartment" }
    UnitRow(
        accent = MBPBlue,
        code = u.code,
        subtitle = sub,
        completion = u.completionPct ?: 0,
        stage = u.currentStage?.name,
        status = u.status,
        isSold = u.isSold,
        onClick = onClick
    )
}

@Composable
internal fun UnitRow(
    accent: Color,
    code: String,
    subtitle: String,
    completion: Int,
    stage: String?,
    status: com.mbp.app.data.model.StatusDto?,
    isSold: Boolean,
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
                Text(code, color = accent, fontWeight = FontWeight.Bold, fontSize = 18.sp,
                    modifier = Modifier.weight(1f))
                if (isSold) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(MBPSuccess.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text("SOLD", color = MBPSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(subtitle, color = MBPGray, fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))
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
                Spacer(Modifier.padding(horizontal = 6.dp))
                Text("$completion%", color = Color.White, fontSize = 12.sp)
            }
            if (status != null || !stage.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!stage.isNullOrBlank()) {
                        Text(stage, color = MBPGray, fontSize = 11.sp, modifier = Modifier.weight(1f))
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                    if (status != null) StatusBadge(status)
                }
            }
        }
    }
}
