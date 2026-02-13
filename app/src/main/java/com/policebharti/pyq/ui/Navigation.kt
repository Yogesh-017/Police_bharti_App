package com.policebharti.pyq.ui

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.policebharti.pyq.ui.auth.AuthScreen
import com.policebharti.pyq.ui.bookmarks.BookmarksScreen
import com.policebharti.pyq.ui.category.CategoryScreen
import com.policebharti.pyq.ui.components.AiHelpModal
import com.policebharti.pyq.ui.components.VoteModal
import com.policebharti.pyq.ui.paused.PausedTestsScreen
import com.policebharti.pyq.ui.results.ResultScreen
import com.policebharti.pyq.ui.selection.SelectionScreen
import com.policebharti.pyq.ui.splash.SplashScreen
import com.policebharti.pyq.ui.test.TestScreen

/**
 * Navigation — central NavHost wiring all screens.
 *
 * Route structure:
 *   splash → login → category → selection → test/{d}/{y}/{s}
 *                                          → paused → test (resume)
 *                                          → bookmarks
 *                         test → result/{correct}/{wrong}/{unanswered}/{total}
 */

sealed class Screen(val route: String) {
    object Splash     : Screen("splash")
    object Login      : Screen("login")
    object Category   : Screen("category")
    object Selection  : Screen("selection")
    object Test       : Screen("test/{district}/{year}/{setName}") {
        fun createRoute(d: String, y: String, s: String) = "test/$d/$y/$s"
    }
    object PausedTests : Screen("paused_tests")
    object Bookmarks   : Screen("bookmarks")
    object Result      : Screen("result/{correct}/{wrong}/{unanswered}/{total}") {
        fun createRoute(c: Int, w: Int, u: Int, t: Int) = "result/$c/$w/$u/$t"
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Modal states
    var showAiModal by remember { mutableStateOf(false) }
    var showVoteModal by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // ── Splash ──
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Login / Signup ──
        composable(Screen.Login.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Category.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Category Select ──
        composable(Screen.Category.route) {
            CategoryScreen(
                onCategorySelected = {
                    navController.navigate(Screen.Selection.route)
                }
            )
        }

        // ── Content Selection (District → Year → Paper) ──
        composable(Screen.Selection.route) {
            SelectionScreen(
                onStartTest = { district, year, set ->
                    navController.navigate(Screen.Test.createRoute(district, year, set))
                },
                onViewPausedTests = {
                    navController.navigate(Screen.PausedTests.route)
                },
                onViewBookmarks = {
                    navController.navigate(Screen.Bookmarks.route)
                }
            )
        }

        // ── Test Screen ──
        composable(
            route = Screen.Test.route,
            arguments = listOf(
                navArgument("district") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType },
                navArgument("setName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val district = backStackEntry.arguments?.getString("district") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""
            val setName = backStackEntry.arguments?.getString("setName") ?: ""

            TestScreen(
                district = district,
                year = year,
                setName = setName,
                onPause = {
                    navController.popBackStack()
                },
                onSubmit = {
                    // Demo: navigate to result with sample scores
                    navController.navigate(Screen.Result.createRoute(72, 18, 10, 100)) {
                        popUpTo(Screen.Selection.route)
                    }
                },
                onShowAiHelp = { showAiModal = true }
            )
        }

        // ── Paused Tests ──
        composable(Screen.PausedTests.route) {
            PausedTestsScreen(
                onResume = { sessionId ->
                    // In production: load session from DB and navigate with session data
                    navController.navigate(Screen.Test.createRoute("Pune", "2023", "Set A"))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Bookmarks ──
        composable(Screen.Bookmarks.route) {
            BookmarksScreen(
                onBack = { navController.popBackStack() },
                onQuestionTap = { /* Navigate to question detail in future */ }
            )
        }

        // ── Result Screen ──
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("wrong") { type = NavType.IntType },
                navArgument("unanswered") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val wrong = backStackEntry.arguments?.getInt("wrong") ?: 0
            val unanswered = backStackEntry.arguments?.getInt("unanswered") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            val percentage = if (total > 0) (correct.toFloat() / total) * 100f else 0f

            ResultScreen(
                correctCount = correct,
                wrongCount = wrong,
                unansweredCount = unanswered,
                totalQuestions = total,
                scorePercentage = percentage,
                onReviewAnswers = { /* Navigate to review screen in future */ },
                onRetake = {
                    navController.navigate(Screen.Selection.route) {
                        popUpTo(Screen.Selection.route) { inclusive = true }
                    }
                },
                onGoHome = {
                    navController.navigate(Screen.Selection.route) {
                        popUpTo(Screen.Category.route)
                    }
                },
                onViewVote = { showVoteModal = true }
            )
        }
    }

    // ── Global Modals ──
    AiHelpModal(
        isVisible = showAiModal,
        onDismiss = { showAiModal = false },
        onVote = {
            showAiModal = false
            showVoteModal = true
        }
    )

    VoteModal(
        isVisible = showVoteModal,
        onVoteYes = { showVoteModal = false },
        onVoteNo = { showVoteModal = false },
        onDismiss = { showVoteModal = false }
    )
}
