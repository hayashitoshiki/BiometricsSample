package com.myapp.biometricssample.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapp.biometricssample.ui.screen.HomeScreen
import com.myapp.biometricssample.ui.screen.LoginScreen

/**
 * 画面定義
 *
 * @property route 遷移パス
 * @property screenName ナビゲーションタイトル
 */
sealed class Screens(
    val route: String,
    val screenName: String
) {

    // DiaryActivity
    object Login : Screens("login_route", "ログイン")
    object Home : Screens("home_route", "ホーム")
}

/**
 * 振り返り日記Activity用 NavigationHost
 *
 */
@Composable
fun DiaryAppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(route = Screens.Login.route) { LoginScreen(navController) }
        composable(route = Screens.Home.route) { HomeScreen() }
    }
}