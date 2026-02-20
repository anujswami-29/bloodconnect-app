package com.example.androidapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androidapp.nav.Route
import com.example.androidapp.screens.DetailScreen
import com.example.androidapp.screens.DonorDashboard
import com.example.androidapp.screens.DonorPassportScreen
import com.example.androidapp.screens.DonorRequestsScreen
import com.example.androidapp.screens.LoginScreen
import com.example.androidapp.screens.OpsDashboard
import com.example.androidapp.screens.CreateRequestScreen
import com.example.androidapp.screens.RequestDetailsScreen
import com.example.androidapp.screens.RegisterScreen
import com.example.androidapp.screens.SettingsScreen
import com.example.androidapp.screens.HelpCenterScreen
import com.example.androidapp.screens.FAQsScreen
import com.example.androidapp.screens.ContactScreen
import com.example.androidapp.screens.AboutScreen
import com.example.androidapp.screens.DonationJourneyScreen
import com.example.androidapp.screens.DonationTipsScreen
import com.example.androidapp.screens.GamificationScreen
import com.example.androidapp.screens.AdminCommandCenterScreen
import com.example.androidapp.screens.AIAssistantScreen
import com.example.androidapp.screens.PredictiveAnalyticsScreen
import com.example.androidapp.state.AuthViewModel
import com.example.androidapp.state.GamificationViewModel
import com.example.androidapp.state.ItemsViewModel
import com.example.androidapp.state.RequestViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val itemsVm: ItemsViewModel = viewModel()
    val authVm: AuthViewModel = viewModel()
    val requestVm: RequestViewModel = viewModel()
    val gamificationVm: GamificationViewModel = viewModel()

    val onOpenSettings = remember(navController) { { navController.navigate(Route.Settings.value) } }
    val onOpenDetail = remember(navController) { { id: String -> navController.navigate(Route.Detail.create(id)) } }

    NavHost(
        navController = navController,
        startDestination = Route.Login.value,
    ) {
        composable(Route.Login.value) {
            LoginScreen(
                auth = authVm,
                onLoginSuccess = {
                    val target = if (authVm.role == com.example.androidapp.state.UserRole.DONOR) {
                        Route.DonorHome.value
                    } else {
                        Route.OpsHome.value
                    }
                    navController.navigate(target) {
                        popUpTo(Route.Login.value) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Route.Register.value)
                },
            )
        }
        composable(Route.Register.value) {
            RegisterScreen(
                auth = authVm,
                onRegisterSuccess = {
                    val target = if (authVm.role == com.example.androidapp.state.UserRole.DONOR) {
                        Route.DonorHome.value
                    } else {
                        Route.OpsHome.value
                    }
                    navController.navigate(target) {
                        popUpTo(Route.Login.value) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.popBackStack()
                },
            )
        }
        composable(Route.DonorHome.value) {
            DonorDashboard(
                onOpenSettings = { navController.navigate(Route.Settings.value) },
                onOpenRequests = {
                    requestVm.refresh()
                    navController.navigate(Route.DonorRequests.value)
                },
                onOpenPassport = { navController.navigate(Route.DonorPassport.value) },
                onOpenGamification = { navController.navigate(Route.Gamification.value) },
            )
        }
        composable(Route.OpsHome.value) {
            val role = authVm.role ?: com.example.androidapp.state.UserRole.ADMIN
            OpsDashboard(
                role = role,
                onOpenSettings = { navController.navigate(Route.Settings.value) },
                onCreateRequest = { navController.navigate(Route.CreateRequest.value) },
                onViewRequests = {
                    requestVm.refresh()
                    val id = requestVm.activeRequests.firstOrNull()?.id ?: ""
                    if (id.isNotEmpty()) navController.navigate(Route.RequestDetails.create(id))
                },
                onOpenCommandCenter = { navController.navigate(Route.AdminCommandCenter.value) },
                onOpenAIAssistant = { navController.navigate(Route.AIAssistant.value) },
                onOpenAnalytics = { navController.navigate(Route.PredictiveAnalytics.value) },
            )
        }
        composable(Route.CreateRequest.value) {
            CreateRequestScreen(
                vm = requestVm,
                onCreated = { req ->
                    navController.navigate(Route.RequestDetails.create(req.id))
                },
            )
        }
        composable(Route.DonorRequests.value) {
            requestVm.refresh()
            DonorRequestsScreen(
                vm = requestVm,
                onBack = { navController.popBackStack() },
                onOpenSettings = { navController.navigate(Route.Settings.value) },
                onAcceptRequest = { id ->
                    navController.navigate(Route.DonationJourney.create(id))
                },
            )
        }
        composable(
            route = Route.DonationJourney.value,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            DonationJourneyScreen(
                vm = requestVm,
                requestId = id,
                onBack = { navController.popBackStack() },
                onDone = {
                    navController.navigate(Route.DonorHome.value) {
                        popUpTo(Route.DonorRequests.value) { inclusive = true }
                    }
                },
            )
        }
        composable(Route.DonorPassport.value) {
            DonorPassportScreen(
                vm = requestVm,
                gamificationVm = gamificationVm,
                onBack = { navController.popBackStack() },
                onOpenGamification = { navController.navigate(Route.Gamification.value) },
            )
        }
        composable(Route.Gamification.value) {
            GamificationScreen(vm = gamificationVm, onBack = { navController.popBackStack() })
        }
        composable(Route.AdminCommandCenter.value) {
            AdminCommandCenterScreen(vm = requestVm, onBack = { navController.popBackStack() })
        }
        composable(Route.AIAssistant.value) {
            val pending = requestVm.activeRequests.count { (System.currentTimeMillis() - it.createdAtMillis) > 30 * 60 * 1000 }
            AIAssistantScreen(
                onBack = { navController.popBackStack() },
                activeRequestCount = requestVm.activeRequests.size,
                pendingOver30Min = pending,
            )
        }
        composable(Route.PredictiveAnalytics.value) {
            PredictiveAnalyticsScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.Settings.value) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    authVm.logout()
                    navController.navigate(Route.Login.value) {
                        popUpTo(Route.Login.value) { inclusive = true }
                    }
                },
                onHelpCenter = { navController.navigate(Route.HelpCenter.value) },
                onFAQs = { navController.navigate(Route.FAQs.value) },
                onContact = { navController.navigate(Route.Contact.value) },
                onAbout = { navController.navigate(Route.About.value) },
                onDonationTips = { navController.navigate(Route.DonationTips.value) },
            )
        }
        composable(Route.HelpCenter.value) {
            HelpCenterScreen(
                onBack = { navController.popBackStack() },
                onNavigate = { section ->
                    when (section) {
                        "faqs" -> navController.navigate(Route.FAQs.value)
                        "contact" -> navController.navigate(Route.Contact.value)
                        "tips" -> navController.navigate(Route.DonationTips.value)
                        "about" -> navController.navigate(Route.About.value)
                        else -> { }
                    }
                },
            )
        }
        composable(Route.FAQs.value) {
            FAQsScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.Contact.value) {
            ContactScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.About.value) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.DonationTips.value) {
            DonationTipsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Route.Detail.value,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            DetailScreen(
                id = id,
                vm = itemsVm,
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = Route.RequestDetails.value,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id").orEmpty()
            RequestDetailsScreen(
                vm = requestVm,
                requestId = id,
            )
        }
    }
}

