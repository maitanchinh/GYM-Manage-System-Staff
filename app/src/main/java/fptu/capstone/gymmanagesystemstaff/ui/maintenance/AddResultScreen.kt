package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import android.Manifest
import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.TextField
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.deleteFile
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.getBitmapFromUri
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.saveBitmapToFile
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.utils.parseDateTime
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddResultScreen(
    maintainId: String,
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val resultState by maintennanceViewModel.result.collectAsState()
    val date by maintennanceViewModel.date.collectAsState()
    val cost by maintennanceViewModel.cost.collectAsState()
    val resultImage by maintennanceViewModel.resultImage.collectAsState()
    val resultBitmap by maintennanceViewModel.resultImageBitmap.collectAsState()
    val invoiceImageBitmap by maintennanceViewModel.invoiceImageBitmap.collectAsState()
    var showSelectImageDialog by remember { mutableStateOf(false) }
    var showSelectInvoiceImageDialog by remember { mutableStateOf(false) }
    var resultImagePath by remember { mutableStateOf<File?>(null) }
    var invoiceImagePath by remember { mutableStateOf<File?>(null) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val launcherTakePhoto =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            resultImagePath = saveBitmapToFile(it)
            if (showSelectImageDialog) {

                maintennanceViewModel.setResultImageBitmap(it)
                maintennanceViewModel.setResultImage(resultImagePath)
                showSelectImageDialog = false
            } else if (showSelectInvoiceImageDialog) {
                maintennanceViewModel.setInvoiceImageBitmap(it)
                maintennanceViewModel.setInvoiceImage(resultImagePath)
                showSelectInvoiceImageDialog = false
            }

        }
    val launcherPickImage =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            invoiceImagePath = saveBitmapToFile(it?.let {
                getBitmapFromUri(
                    context,
                    it
                )
            })
            if (showSelectImageDialog) {
                maintennanceViewModel.setResultImageBitmap(it?.let {
                    getBitmapFromUri(
                        context,
                        it
                    )
                })
                maintennanceViewModel.setResultImage(invoiceImagePath)
                showSelectImageDialog = false
            } else if (showSelectInvoiceImageDialog) {
                maintennanceViewModel.setInvoiceImageBitmap(it?.let {
                    getBitmapFromUri(
                        context,
                        it
                    )
                })
                maintennanceViewModel.setInvoiceImage(invoiceImagePath)
                showSelectInvoiceImageDialog = false
            }
        }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
            val localDateTime = LocalDateTime.of(
                selectedYear,
                selectedMonth + 1,
                selectedDay,
                0,
                0, 0, 0
            )
            maintennanceViewModel.onDateChange(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        }, year, month, day
    )
    LaunchedEffect(resultState) {
        if (resultState is DataState.Success) {
            Toast.makeText(context, "Add result successfully", Toast.LENGTH_SHORT).show()
            onNavigateBack()
        } else if (resultState is DataState.Error) {
            Toast.makeText(context, (resultState as DataState.Error).message, Toast.LENGTH_SHORT)
                .show()
        }
        if (resultImagePath != null) {
            deleteFile(resultImagePath!!.absolutePath)
        }
        if (invoiceImagePath != null) {
            deleteFile(invoiceImagePath!!.absolutePath)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(shape = RoundedCornerShape(32.dp))
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    showSelectImageDialog = true
                }
        ) {
            if (resultBitmap == null)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_camera_alt_24),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Gap.k16.Height()
                    Text(
                        text = "Take or select a image of the result",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )

                }
            else
                Image(
                    bitmap = resultBitmap!!.asImageBitmap(),
                    contentDescription = "image",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )

        }
        Gap.k16.Height()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.White)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    datePickerDialog.show()
                }, contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = if (date.isNotEmpty()) parseDateTime(date).format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ) else "Choose a date",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Gap.k16.Height()
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(1f),
                value = cost,
                onTextChange = { newValue ->
                    maintennanceViewModel.onCostChange(newValue)
                },
                label = "Cost",
                keyboardOption = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Gap.k16.Width()
            Text(
                text = "VND",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Gap.k16.Width()
        }
        Gap.k16.Height()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(shape = RoundedCornerShape(32.dp))
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .clickable { showSelectInvoiceImageDialog = true }
        ) {
            if (invoiceImageBitmap == null)
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_camera_alt_24),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                    Gap.k16.Height()
                    Text(
                        text = "Take or select a image of invoice",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            else
                Image(
                    bitmap = invoiceImageBitmap!!.asImageBitmap(),
                    contentDescription = "image",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
        }
        Gap.k32.Height()
        LargeButton(
            text = "Save",
            isLoading = resultState is DataState.Loading,
            enabled = date.isNotEmpty() && cost != null && resultImage != null
        ) {
            maintennanceViewModel.addMaintainResult(maintainId)

        }
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
        AlertDialog(
            onDismissRequest = { showSelectImageDialog = false },
            confirmButton = { /*TODO*/ },
            dismissButton = {
                Text(
                    modifier = Modifier.clickable { showSelectImageDialog = false },
                    text = "Cancel"
                )
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable(onClick = {
                                if (cameraPermissionState.status.isGranted)
                                    launcherTakePhoto.launch()
                                else {
                                    takePhotoPending = true
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
                                if (galleryPermissionState.status.isGranted)
                                    launcherPickImage.launch("image/*")
                                else {
                                    pickImagePending = true
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
    if (showSelectInvoiceImageDialog) {
        var takeInvoicePhotoPending by remember { mutableStateOf(false) }
        var pickInvoiceImagePending by remember { mutableStateOf(false) }

        if (takeInvoicePhotoPending && cameraPermissionState.status.isGranted) {
            LaunchedEffect(Unit) {
                takeInvoicePhotoPending = false
                launcherTakePhoto.launch()
            }
        }

        if (pickInvoiceImagePending && galleryPermissionState.status.isGranted) {
            LaunchedEffect(Unit) {
                pickInvoiceImagePending = false
                launcherPickImage.launch("image/*")
            }
        }
        AlertDialog(
            onDismissRequest = { showSelectInvoiceImageDialog = false },
            confirmButton = { /*TODO*/ },
            dismissButton = {
                Text(modifier = Modifier.clickable {
                    showSelectInvoiceImageDialog = false
                }, text = "Cancel")
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable(onClick = {
                                if (cameraPermissionState.status.isGranted)
                                    launcherTakePhoto.launch()
                                else {
                                    takeInvoicePhotoPending = true
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
                                if (galleryPermissionState.status.isGranted)
                                    launcherPickImage.launch("image/*")
                                else {
                                    pickInvoiceImagePending = true
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
}