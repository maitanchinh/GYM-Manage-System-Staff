package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.PickupTab
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.ResultTab

@Composable
fun Maintain(
    maintainId: String,
    onResultClick: (resultId: String) -> Unit,
    onUpdateResultClick: (maintainId: String) -> Unit,
    onAddPickupClick: (maintainId: String) -> Unit,
    navController: NavController
) {
    val tabs = listOf("Pickup", "Result")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PickupTab(
            maintainId = maintainId,
            onAddPickupClick = onAddPickupClick,
            onUpdateResultClick = onUpdateResultClick,
            navController = navController
        )
        Gap.k16.Height()

        ResultTab(maintainId = maintainId, navController = navController, onResultClick = onResultClick)

    }
}