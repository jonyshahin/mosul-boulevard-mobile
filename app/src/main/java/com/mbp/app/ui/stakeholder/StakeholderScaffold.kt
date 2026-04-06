package com.mbp.app.ui.stakeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mbp.app.ui.customer.UnitDetailScreen
import com.mbp.app.ui.theme.MBPDark
import com.mbp.app.ui.theme.MBPGold
import com.mbp.app.ui.theme.MBPGray
import com.mbp.app.ui.theme.MBPSurface

object StkRoutes {
    const val DASHBOARD = "stk_dashboard"
    const val VILLAS = "stk_villas"
    const val TOWERS = "stk_towers"
    const val REPORTS = "stk_reports"
    const val MORE = "stk_more"
    const val CUSTOMERS = "stk_customers"
    const val CUSTOMER_DETAIL = "stk_customer/{id}"
    const val UNIT_DETAIL = "stk_unit/{unitId}/{unitType}"
    fun customer(id: Int) = "stk_customer/$id"
    fun unit(id: Int, type: String) = "stk_unit/$id/$type"
}

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem(StkRoutes.DASHBOARD, "Dashboard", Icons.Filled.GridView),
    TabItem(StkRoutes.VILLAS, "Villas", Icons.Filled.HomeWork),
    TabItem(StkRoutes.TOWERS, "Towers", Icons.Filled.Apartment),
    TabItem(StkRoutes.REPORTS, "Reports", Icons.Filled.BarChart),
    TabItem(StkRoutes.MORE, "More", Icons.Filled.MoreHoriz),
)

@Composable
fun StakeholderScaffold(onLoggedOut: () -> Unit) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = tabs.any { it.route == currentRoute } || currentRoute == null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MBPDark,
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = MBPSurface) {
                    tabs.forEach { tab ->
                        val selected = currentRoute == tab.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    navController.navigate(tab.route) {
                                        popUpTo(StkRoutes.DASHBOARD) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MBPGold,
                                selectedTextColor = MBPGold,
                                unselectedIconColor = MBPGray,
                                unselectedTextColor = MBPGray,
                                indicatorColor = Color.Transparent,
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = StkRoutes.DASHBOARD,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MBPDark)
        ) {
            composable(StkRoutes.DASHBOARD) {
                StakeholderDashboardScreen(onLogout = onLoggedOut)
            }
            composable(StkRoutes.VILLAS) {
                VillasListScreen(
                    onOpenVilla = { id -> navController.navigate(StkRoutes.unit(id, "villa")) }
                )
            }
            composable(StkRoutes.TOWERS) {
                TowersListScreen(
                    onOpenUnit = { id -> navController.navigate(StkRoutes.unit(id, "tower")) }
                )
            }
            composable(StkRoutes.REPORTS) { ReportsScreen() }
            composable(StkRoutes.MORE) {
                MoreScreen(
                    onCustomers = { navController.navigate(StkRoutes.CUSTOMERS) },
                    onLogout = onLoggedOut,
                )
            }
            composable(StkRoutes.CUSTOMERS) {
                StakeholderCustomersListScreen(
                    onBack = { navController.popBackStack() },
                    onOpenCustomer = { id -> navController.navigate(StkRoutes.customer(id)) }
                )
            }
            composable(
                StkRoutes.CUSTOMER_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                StakeholderCustomerDetailScreen(
                    customerId = id,
                    onBack = { navController.popBackStack() },
                    onOpenVilla = { vid -> navController.navigate(StkRoutes.unit(vid, "villa")) },
                    onOpenUnit = { uid -> navController.navigate(StkRoutes.unit(uid, "tower")) },
                )
            }
            composable(
                StkRoutes.UNIT_DETAIL,
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
                    onOpenSiteUpdates = { _, _ -> /* stakeholder skips */ }
                )
            }
        }
    }
}
