package fptu.capstone.gymmanagesystemstaff.ui.navigation

sealed class Route(val route: String) {
    object Login : Route("login")
    object Dashboard : Route("dashboard")
    object Profile : Route("profile")
    object Home : Route("home")
    object Signup : Route("signup")
    object ProfileDetail : Route("profileDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "profileDetail/$id"
        }
    }
    object Class : Route("class")
    object AllClass : Route("allClass")
    object ClassDetail : Route("classDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "classDetail/$id"
        }
    }
    object Attendance : Route("home/attendance")
    object Checkin : Route("home/checkin")
}