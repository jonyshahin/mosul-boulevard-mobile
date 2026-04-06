package com.mbp.app.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mbp.app.data.model.SiteUpdateDto
import com.mbp.app.ui.theme.MBPBlue
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPError
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun SiteUpdatesScreen(
    unitId: Int,
    unitType: String,
    onBack: () -> Unit,
    viewModel: UnitDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(unitId, unitType) { viewModel.load(unitId, unitType) }

    val accent = if (unitType == "villa") MBPGold else MBPBlue
    val updates: List<SiteUpdateDto> = state.villa?.siteUpdates
        ?: state.towerUnit?.siteUpdates
        ?: emptyList()

    var fullscreenUrl by remember { mutableStateOf<String?>(null) }

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
            Text("Site Updates", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accent)
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = MBPError)
            }
            updates.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No site updates yet", color = MBPGray)
            }
            else -> LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(updates, key = { it.id }) { upd ->
                    UpdateCard(upd, accent) { fullscreenUrl = it }
                }
            }
        }
    }

    if (fullscreenUrl != null) {
        Dialog(onDismissRequest = { fullscreenUrl = null }) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable { fullscreenUrl = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = fullscreenUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
private fun UpdateCard(
    update: SiteUpdateDto,
    accent: Color,
    onPhotoClick: (String) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                update.date ?: "",
                color = accent,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            if (!update.notes.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(update.notes, color = Color.White, fontSize = 14.sp)
            }
            val photos = update.photos.orEmpty().mapNotNull { it.url }
            if (photos.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(photos) { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MBPDark)
                                .clickable { onPhotoClick(url) }
                        )
                    }
                }
            }
        }
    }
}
