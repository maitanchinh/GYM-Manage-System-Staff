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
    object Maintenance : Route("maintenance")
    object Maintain : Route("maintenance/maintain/{id}"){
        fun createRouteWithId(id: String): String {
            return "maintenance/maintain/$id"
        }
    }
    object ResultDetail : Route("maintenance/resultDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "maintenance/resultDetail/$id"
        }
    }
    object AddMaintain : Route("maintenance/addMaintain")
    object AddPickup : Route("maintenance/addPickup/{id}"){
        fun createRouteWithId(id: String): String {
            return "maintenance/addPickup/$id"
        }
    }
    object AddResult : Route("maintenance/addResult/{id}"){
        fun createRouteWithId(id: String): String {
            return "maintenance/addResult/$id"
        }
    }
    object SlotEquipment : Route("equipment")
    object SlotEquipmentDetail : Route("equipment/equipmentDetail/{id}"){
        fun createRouteWithId(id: String): String {
            return "equipment/equipmentDetail/$id"
        }
    }
}