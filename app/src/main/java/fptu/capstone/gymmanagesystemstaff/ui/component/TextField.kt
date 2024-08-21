@file:OptIn(ExperimentalMaterial3Api::class)

package fptu.capstone.gymmanagesystemstaff.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import fptu.capstone.gymmanagesystemstaff.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = 1,
    suffix: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    onTextChange: (String) -> Unit,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val currentVisualTransformation = if (isPasswordVisible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        enabled = enabled,
        value = value ?: "",
        onValueChange = onTextChange,
        keyboardOptions = keyboardOption,
//        label = { Text(label!!) },
        placeholder = { label?.let { Text(it) } },
        modifier = modifier
//            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray,
        ),
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (visualTransformation is PasswordVisualTransformation) {
            currentVisualTransformation
        } else {
            visualTransformation
        },
        readOnly = readOnly,
        maxLines = maxLines,
        minLines = maxLines,
        suffix = {
            if (visualTransformation is PasswordVisualTransformation) {

                if (isPasswordVisible) {
                    Icon(
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible },
                        painter = painterResource(id = R.drawable.round_visibility_off_24),
                        contentDescription = "Hide password"
                    )
                } else {
                    Icon(
                        modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible },
                        painter = painterResource(id = R.drawable.round_visibility_24),
                        contentDescription = "Show password"
                    )
                }

            }
            suffix?.invoke()
        },
        prefix = prefix,
        trailingIcon = trailingIcon
    )
}

