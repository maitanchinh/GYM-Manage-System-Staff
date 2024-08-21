package fptu.capstone.gymmanagesystemstaff.ui.maintenance.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.UploadImage
import fptu.capstone.gymmanagesystemstaff.ui.component.shimmerLoadingAnimation
import fptu.capstone.gymmanagesystemstaff.ui.navigation.Route
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PickupTab(
    maintainId: String,
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel(),
    navController: NavController,
    onAddPickupClick: (maintainId: String) -> Unit,
    onUpdateResultClick: (maintainId: String) -> Unit
) {
    val context = LocalContext.current
    val pickupsState by maintennanceViewModel.pickups.collectAsState()
    val pickupState by maintennanceViewModel.pickup.collectAsState()
    val resultsState by maintennanceViewModel.results.collectAsState()
    val addPickupState by maintennanceViewModel.addPickup.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var showSelectImageDialog by remember { mutableStateOf(false) }
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    var pickedImagePath by remember { mutableStateOf<File?>(null) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val launcherTakePhoto =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            pickedImagePath = it?.let { saveBitmapToFile(it) }
            maintennanceViewModel.setImageBitmap(it)
            maintennanceViewModel.setPickupImage(pickedImagePath)
            maintennanceViewModel.addMaintainPickup(maintainId = maintainId)
            showSelectImageDialog = false

        }
    val launcherPickImage =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            pickedImagePath = saveBitmapToFile(it?.let {
                getBitmapFromUri(
                    context,
                    it
                )
            })
            maintennanceViewModel.setImageBitmap(it?.let { getBitmapFromUri(context, it) })
            maintennanceViewModel.setPickupImage(pickedImagePath)
            maintennanceViewModel.addMaintainPickup(maintainId = maintainId)
            showSelectImageDialog = false
        }
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    LaunchedEffect(currentBackStackEntry.value) {
        // Reload dữ liệu khi màn hình trở thành màn hình hiện tại
        if (currentBackStackEntry.value?.destination?.route == Route.Maintain.route) {
            maintennanceViewModel.fetchPickups(FilterRequestBody(maintainId = maintainId))
            maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
        }
    }
    LaunchedEffect(Unit) {
        maintennanceViewModel.fetchPickups(FilterRequestBody(maintainId = maintainId))
        maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
    }
    LaunchedEffect(addPickupState) {
        if (addPickupState is DataState.Success) {
            Toast.makeText(context, "Add pickup successfully", Toast.LENGTH_SHORT).show()
            maintennanceViewModel.fetchPickups(FilterRequestBody(maintainId = maintainId))
            if (pickedImagePath != null) {
                deleteFile(pickedImagePath!!.absolutePath)
            }
        } else if (addPickupState is DataState.Error) {
            Toast.makeText(context, "Add pickup failed", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(pickupState) {
        if (pickupState is DataState.Success) {
            Toast.makeText(navController.context, "Delete success", Toast.LENGTH_SHORT).show()
            maintennanceViewModel.fetchPickups(FilterRequestBody(maintainId = maintainId))
        } else if (pickupState is DataState.Error) {
            Toast.makeText(
                navController.context,
                (pickupState as DataState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            maintennanceViewModel.fetchPickups(FilterRequestBody(maintainId = maintainId))
            isRefreshing = false
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = if (pickupsState is DataState.Success && ((pickupsState as DataState.Success).data.data.isEmpty())) Arrangement.Center else Arrangement.Top
        ) {
            Text(text = "Pickup", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Gap.k16.Height()
            if (pickupsState is DataState.Success && ((pickupsState as DataState.Success).data.data.isEmpty())) LargeButton(text = "Add Pickup", isLoading = addPickupState is DataState.Loading) {
                showSelectImageDialog = true
            }
            when (pickupsState) {
                is DataState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .shimmerLoadingAnimation()
                    ) {

                    }
                }

                is DataState.Success -> {
                    val pickups = (pickupsState as DataState.Success).data.data
                    if (pickups.isEmpty()) {
                        Text(modifier = Modifier.fillMaxSize(), text = "No pickups", textAlign = TextAlign.Center)
                    } else
                        PickupItem(onUpdateResultClick =  {
                            onUpdateResultClick(maintainId)
                        }, onDeleteClick = { isShowDeleteDialog = true }, pickup = pickups.first().copy(beAbleToUpdate = if (resultsState is DataState.Success) (resultsState as DataState.Success).data.data.isEmpty() else false))
                }

                is DataState.Error -> {
                    Text(text = (pickupsState as DataState.Error).message)
                }

                else -> {}
            }
            if (showSelectImageDialog) {
                var takePhotoPending by remember { mutableStateOf(false) }
                var pickImagePending by remember { mutableStateOf(false) }

                if (takePhotoPending && cameraPermissionState.status.isGranted) {
                    LaunchedEffect(Unit) {
                        takePhotoPending = false
                        launcherTakePhoto.launch()
                    }
                }

                if (pickImagePending && galleryPermissionState.status.isGranted) {
                    LaunchedEffect(Unit) {
                        pickImagePending = false
                        launcherPickImage.launch("image/*")
                    }
                }
                UploadImage(
                    cameraPermissionState,
                    launcherTakePhoto,
                    galleryPermissionState,
                    launcherPickImage,
                    onDismissRequest = { showSelectImageDialog = false }
                )
            }
            if (isShowDeleteDialog) {
                AlertDialog(onDismissRequest = { isShowDeleteDialog = false }, confirmButton = {
                    TextButton(onClick = {
                        maintennanceViewModel.deleteMaintainPickup((pickupsState as DataState.Success).data.data.first().id!!)
                        isShowDeleteDialog = false
                    }) {
                        Text(text = "Yes")
                    }
                },
                    dismissButton = {
                        TextButton(onClick = { isShowDeleteDialog = false }) {
                            Text(text = "No")
                        }
                    },
                    text = {
                        Text(text = "Do you want to delete this pickup?")
                    })
            }
        }
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveBitmapToFile(bitmap: Bitmap?): File? {
    val file = File.createTempFile("communication", ".jpg")
    try {
        val outputStream = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return file
}

fun deleteFile(filePath: String) {
    val file = File(filePath)
    if (file.exists()) {
        file.delete()
    }
}
