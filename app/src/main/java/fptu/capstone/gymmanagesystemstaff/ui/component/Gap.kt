package fptu.capstone.gymmanagesystemstaff.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object Gap {
    val k4 : GapDimension = GapDimension(4.dp)
    val k8 : GapDimension = GapDimension(8.dp)
    val k16 : GapDimension = GapDimension(16.dp)
    val k24 : GapDimension = GapDimension(24.dp)
    val k32 : GapDimension = GapDimension(32.dp)
}

data class GapDimension(val value: Dp){
    @Composable
    fun Height() {
        Spacer(modifier = Modifier.height(value))
    }

    @Composable
    fun Width() {
        Spacer(modifier = Modifier.width(value))
    }
}
