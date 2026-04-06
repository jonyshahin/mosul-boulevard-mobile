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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.ui.auth.mbpFieldColors
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPError
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

@Composable
fun StakeholderCustomersListScreen(
    onBack: () -> Unit,
    onOpenCustomer: (Int) -> Unit,
    viewModel: CustomersListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
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
            Text("Customers", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        }
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

        val listState = rememberLazyListState()
        val shouldLoadMore by remember {
            derivedStateOf {
                val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                last >= state.items.size - 3 && state.canLoadMore && !state.isLoading
            }
        }
        LaunchedEffect(shouldLoadMore) { if (shouldLoadMore) viewModel.loadMore() }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.items, key = { it.id }) { c ->
                CustomerRow(c) { onOpenCustomer(c.id) }
            }
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
private fun CustomerRow(c: CustomerDto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MBPSurface),
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(c.name, color = Color.White, fontWeight = FontWeight.SemiBold)
            if (!c.phone.isNullOrBlank()) {
                Text(c.phone, color = MBPGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StakeholderCustomerDetailScreen(
    customerId: Int,
    onBack: () -> Unit,
    onOpenVilla: (Int) -> Unit,
    onOpenUnit: (Int) -> Unit,
    viewModel: StakeholderCustomerDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(customerId) { viewModel.load(customerId) }

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
            Text("Customer", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        }
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MBPGold)
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "", color = MBPError)
            }
            state.customer != null -> {
                val c = state.customer!!
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MBPSurface),
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(c.name, color = Color.White, fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp)
                                Spacer(Modifier.height(8.dp))
                                InfoLine("Phone", c.phone)
                                InfoLine("Email", c.email)
                                InfoLine("Address", c.address)
                                if (!c.notes.isNullOrBlank()) InfoLine("Notes", c.notes)
                            }
                        }
                    }
                    val villas = c.villas.orEmpty()
                    val units = c.towerUnits.orEmpty()
                    if (villas.isNotEmpty()) {
                        item {
                            Text("Villas", color = MBPGold, fontWeight = FontWeight.SemiBold)
                        }
                        items(villas, key = { it.id }) { v -> VillaRow(v) { onOpenVilla(v.id) } }
                    }
                    if (units.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(4.dp))
                            Text("Tower Units", color = MBPGold, fontWeight = FontWeight.SemiBold)
                        }
                        items(units, key = { it.id }) { u -> TowerRow(u) { onOpenUnit(u.id) } }
                    }
                    if (villas.isEmpty() && units.isEmpty()) {
                        item { Text("No linked units", color = MBPGray) }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String?) {
    if (value.isNullOrBlank()) return
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(label, color = MBPGray, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}
