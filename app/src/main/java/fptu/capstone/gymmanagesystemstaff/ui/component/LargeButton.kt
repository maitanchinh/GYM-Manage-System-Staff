package fptu.capstone.gymmanagesystemstaff.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LargeButton(modifier: Modifier = Modifier, text: String, isLoading: Boolean, enabled: Boolean = true, isAlter : Boolean = false, onClick: () -> Unit, ) {
    Button(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = if (isAlter) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
            contentColor = if (isAlter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            disabledContainerColor = if (isAlter) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            disabledContentColor = Color.White
        ),
        border = if (isAlter) BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary) else null,
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp).size(20.dp), color = Color.White)
        } else {
            Text(text, modifier = Modifier.padding(8.dp), color = if (isAlter) MaterialTheme.colorScheme.primary else Color.White, style = MaterialTheme.typography.titleMedium)
        }
    }
}