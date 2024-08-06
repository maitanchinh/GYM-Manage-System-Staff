package fptu.capstone.gymmanagesystemstaff.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.TextField

@Composable
fun SignupScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineLarge)
        Gap.k32.Height()
        TextField(label = "Name", value = "", onTextChange = {})
        Gap.k8.Height()
        TextField(label = "Email", value = "", onTextChange = {})
        Gap.k8.Height()
        TextField(label = "Password", value = "", visualTransformation = PasswordVisualTransformation(), onTextChange = {})
        Gap.k8.Height()
        TextField(label = "Confirm Password", value = "", visualTransformation = PasswordVisualTransformation(), onTextChange = {})
        Gap.k8.Height()
        TextField(label = "Phone", value = "", onTextChange = {})
        Gap.k8.Height()
        TextField(label = "Address", value = "", onTextChange = {})
        Gap.k32.Height()
        LargeButton(text = "Sign Up", isLoading = false, onClick = {})
    }
}