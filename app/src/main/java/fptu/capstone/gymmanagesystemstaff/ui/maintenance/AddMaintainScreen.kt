package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel

@Composable
fun AddMaintainScreen(maintennanceViewModel: MaintennanceViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {

    }
}