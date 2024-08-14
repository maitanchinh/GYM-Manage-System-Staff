package fptu.capstone.gymmanagesystemstaff.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fptu.capstone.gymmanagesystemstaff.ui.attendance.AttendanceScreen
import fptu.capstone.gymmanagesystemstaff.ui.checkin.CheckinScreen
import fptu.capstone.gymmanagesystemstaff.ui.gymclass.AllClassScreen
import fptu.capstone.gymmanagesystemstaff.ui.gymclass.ClassScreen
import fptu.capstone.gymmanagesystemstaff.ui.gymclass.detail.ClassDetailScreen
import fptu.capstone.gymmanagesystemstaff.ui.home.HomeScreen
import fptu.capstone.gymmanagesystemstaff.ui.login.LoginScreen
import fptu.capstone.gymmanagesystemstaff.ui.profile.ProfileDetailScreen
import fptu.capstone.gymmanagesystemstaff.ui.profile.ProfileScreen
import fptu.capstone.gymmanagesystemstaff.ui.signup.SignupScreen
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.AuthViewModel

const val BOTTOM_BAR_ROUTE = "bottomBar"

@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) BottomNavItem.Profile.route else BottomNavItem.Home.route,
        modifier = modifier,
        route = BOTTOM_BAR_ROUTE
    ) {
        composable(BottomNavItem.Home.route) { HomeScreen(
            onCheckInClick = { navController.navigate(Route.Checkin.route) },
            onTrainerAttendanceClick = { navController.navigate(Route.Attendance.route) }
        ) }
        composable(BottomNavItem.Profile.route) {
            if (isLoggedIn) {
                ProfileScreen(onProfileDetailClick = { id ->
                    navController.navigate(Route.ProfileDetail.createRouteWithId(id))
                },
                    isLoading = authState is DataState.Loading,
                    onLogoutClick = {
                        authViewModel.logout()
                    })
            } else {
                LoginScreen(authViewModel, navController = navController)
            }
        }
        composable(BottomNavItem.Class.route) {
            ClassScreen(
                onViewAllMyClassClick = { navController.navigate(Route.AllClass.route) },
                onClassClick = { id -> navController.navigate(Route.ClassDetail.createRouteWithId(id)) })
        }
        composable(Route.Signup.route) {
            SignupScreen()
        }
        composable(Route.ProfileDetail.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            ProfileDetailScreen(userId = userId!!)
        }
        composable(Route.AllClass.route) {
            AllClassScreen(onClassClick = { id ->
                navController.navigate(
                    Route.ClassDetail.createRouteWithId(
                        id
                    )
                )
            })
        }
        composable(Route.ClassDetail.route) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("id")
            ClassDetailScreen(classId = classId!!)
        }
        composable(Route.Attendance.route) {
            AttendanceScreen()
        }
        composable(Route.Checkin.route) {
            CheckinScreen()
        }
    }
}