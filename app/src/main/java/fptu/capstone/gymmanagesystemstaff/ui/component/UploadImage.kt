package fptu.capstone.gymmanagesystemstaff.ui.component

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import fptu.capstone.gymmanagesystemstaff.R

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun UploadImage(
    cameraPermissionState: PermissionState,
    launcherTakePhoto: ManagedActivityResultLauncher<Void?, Bitmap?>,
    galleryPermissionState: PermissionState,
    launcherPickImage: ManagedActivityResultLauncher<String, Uri?>,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = { /*TODO*/ },
        dismissButton = { Text(modifier = Modifier.clickable { onDismissRequest() }, text = "Cancel") },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable(onClick = {
                            onDismissRequest()

                            if (cameraPermissionState.status.isGranted)
                                launcherTakePhoto.launch()
                            else {
                                onDismissRequest()
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_camera_alt_24),
                            contentDescription = "Take Photo"
                        )
                        Gap.k16.Width()
                        Text(text = "Take a photo")
                    }
                }
                Gap.k16.Height()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable(onClick = {
                            onDismissRequest()
                            if (galleryPermissionState.status.isGranted)
                                launcherPickImage.launch("image/*")
                            else {
                                onDismissRequest()
                                galleryPermissionState.launchPermissionRequest()
                            }
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_folder_24),
                            contentDescription = "Choose from Gallery"
                        )
                        Gap.k16.Width()
                        Text(text = "Choose from Gallery")
                    }
                }
            }

        })
}