package fptu.capstone.gymmanagesystemstaff.ui.navigation

import fptu.capstone.gymmanagesystemstaff.R

sealed class BottomNavItem(var title: String, var icon: Int, var route: String) {
    object Home : BottomNavItem("Home", R.drawable.round_home_24, Route.Home.route)
    object Maintenance : BottomNavItem("Maintenance", R.drawable.round_build_24, Route.Maintenance.route)
    object Equipment : BottomNavItem("Equipment", R.drawable.round_fitness_center_24, Route.SlotEquipment.route)
    object Profile : BottomNavItem("Profile", R.drawable.round_person_24, Route.Profile.route)
    object Class : BottomNavItem("Class", R.drawable.round_class_24, Route.Class.route)
//    object Attendance : BottomNavItem("Attendance", R.drawable.round_class_24, Route.Attendance.route)
}