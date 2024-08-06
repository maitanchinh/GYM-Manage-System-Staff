package fptu.capstone.gymmanagesystemstaff.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fptu.capstone.gymmanagesystemstaff.ui.dashboard.DashboardScreen

const val ROOT_ROUTE = "root"
@Composable
fun AppNavigation(modifier: Modifier = Modifier, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Route.Dashboard.route, route = ROOT_ROUTE) {
        composable(Route.Dashboard.route) {
            DashboardScreen(navController = navHostController)
        }
//        composable(Route.Login.route) {
//            LoginScreen(loginViewModel = loginViewModel, onLoginSuccess = {navController.navigate(Route.Dashboard.route)})
//        }
    }
}