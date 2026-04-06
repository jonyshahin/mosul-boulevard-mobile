package com.mbp.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.mbp.app.ui.auth.AuthViewModel
import com.mbp.app.ui.auth.PhoneLoginScreen
import com.mbp.app.ui.auth.RoleSelectionScreen
import com.mbp.app.ui.auth.StakeholderLoginScreen
import com.mbp.app.ui.customer.CustomerHomeScreen
import com.mbp.app.ui.customer.SiteUpdatesScreen
import com.mbp.app.ui.customer.UnitDetailScreen
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold

object Routes {
    const val ROLE_SELECTION = "role_selection"
    const val PHONE_LOGIN = "phone_login"
    const val STAKEHOLDER_LOGIN = "stakeholder_login"
    const val CUSTOMER_HOME = "customer_home"
    const val STAKEHOLDER_DASHBOARD = "stakeholder_dashboard"
    const val UNIT_DETAIL = "unit_detail/{unitId}/{unitType}"
    const val SITE_UPDATES = "site_updates/{unitId}/{unitType}"
    fun unitDetail(id: Int, type: String) = "unit_detail/$id/$type"
    fun siteUpdates(id: Int, type: String) = "site_updates/$id/$type"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val state by authViewModel.state.collectAsState()

    if (state.initializing) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MBPDark),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MBPGold)
        }
        return
    }

    val start = when {
        state.isLoggedIn && state.userRole == "customer" -> Routes.CUSTOMER_HOME
        state.isLoggedIn && state.userRole == "admin" -> Routes.STAKEHOLDER_DASHBOARD
        else -> Routes.ROLE_SELECTION
    }

    NavHost(navController = navController, startDestination = start) {
        composable(Routes.ROLE_SELECTION) {
            RoleSelectionScreen(
                onSelectCustomer = { navController.navigate(Routes.PHONE_LOGIN) },
                onSelectStakeholder = { navController.navigate(Routes.STAKEHOLDER_LOGIN) },
            )
        }
        composable(Routes.PHONE_LOGIN) {
            PhoneLoginScreen(
                onBack = { navController.popBackStack() },
                onLoggedIn = {
                    navController.navigate(Routes.CUSTOMER_HOME) {
                        popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.STAKEHOLDER_LOGIN) {
            StakeholderLoginScreen(
                onBack = { navController.popBackStack() },
                onLoggedIn = {
                    navController.navigate(Routes.STAKEHOLDER_DASHBOARD) {
                        popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.CUSTOMER_HOME) {
            CustomerHomeScreen(
                onOpenVilla = { id ->
                    navController.navigate(Routes.unitDetail(id, "villa"))
                },
                onOpenTowerUnit = { id ->
                    navController.navigate(Routes.unitDetail(id, "tower"))
                },
                onLoggedOut = {
                    navController.navigate(Routes.ROLE_SELECTION) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(
            Routes.UNIT_DETAIL,
            arguments = listOf(
                navArgument("unitId") { type = NavType.IntType },
                navArgument("unitType") { type = NavType.StringType },
            )
        ) { entry ->
            val id = entry.arguments?.getInt("unitId") ?: 0
            val type = entry.arguments?.getString("unitType") ?: "villa"
            UnitDetailScreen(
                unitId = id,
                unitType = type,
                onBack = { navController.popBackStack() },
                onOpenSiteUpdates = { uid, ut ->
                    navController.navigate(Routes.siteUpdates(uid, ut))
                }
            )
        }
        composable(
            Routes.SITE_UPDATES,
            arguments = listOf(
                navArgument("unitId") { type = NavType.IntType },
                navArgument("unitType") { type = NavType.StringType },
            )
        ) { entry ->
            val id = entry.arguments?.getInt("unitId") ?: 0
            val type = entry.arguments?.getString("unitType") ?: "villa"
            SiteUpdatesScreen(
                unitId = id,
                unitType = type,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.STAKEHOLDER_DASHBOARD) { ComingSoon("Stakeholder Dashboard") }
    }
}

@Composable
private fun ComingSoon(label: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MBPDark),
        contentAlignment = Alignment.Center
    ) {
        Text("$label — Coming Soon", color = Color.White)
    }
}
