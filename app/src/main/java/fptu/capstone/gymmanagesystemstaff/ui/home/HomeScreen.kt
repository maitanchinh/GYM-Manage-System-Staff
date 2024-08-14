package fptu.capstone.gymmanagesystemstaff.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onCheckInClick: () -> Unit, onTrainerAttendanceClick: () -> Unit) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.Center) {
        LargeButton(text = "Trainer attendance", isLoading = false) {
            onTrainerAttendanceClick()
        }
        Gap.k16.Height()
        LargeButton(text = "Check-in for member", isLoading = false) {
            onCheckInClick()
        }
    }
}