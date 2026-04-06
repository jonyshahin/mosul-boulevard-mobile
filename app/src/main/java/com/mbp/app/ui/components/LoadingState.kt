package com.mbp.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MBPDark),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MBPGold)
    }
}
