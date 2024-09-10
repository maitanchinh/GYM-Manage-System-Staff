@file:kotlin.OptIn(ExperimentalPermissionsApi::class)

package fptu.capstone.gymmanagesystemstaff.ui.checkin

import android.Manifest
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.theme.Amber
import fptu.capstone.gymmanagesystemstaff.ui.theme.ForestGreen
import fptu.capstone.gymmanagesystemstaff.ui.theme.GoldYellow
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.CheckinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalGetImage
@Composable
fun CheckinScreen(checkinViewModel: CheckinViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var qrCodeContent by remember { mutableStateOf("") }
    val previewView = remember { PreviewView(context) }
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val attendanceState by checkinViewModel.checkinState.collectAsState()
    var isShowInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(attendanceState) {
        if (attendanceState is DataState.Success) {
            val attendance = (attendanceState as DataState.Success).data
            Toast.makeText(context, "Check-in success", Toast.LENGTH_SHORT).show()
            isShowInfoDialog = true
        } else if (attendanceState is DataState.Error) {
            qrCodeContent = ""
            Toast.makeText(
                context,
                (attendanceState as DataState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (cameraPermissionState.status.isGranted) {
            val cameraProviderFutures = ProcessCameraProvider.getInstance(context)
            cameraProviderFutures.addListener({
                val cameraProvider = cameraProviderFutures.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                val barcodeScanner = BarcodeScanning.getClient()
                val analysisUseCase =
                    ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )
                        barcodeScanner.process(image)
                            .addOnSuccessListener { barcodes ->
                                barcodes.forEach { barcode ->
                                    barcode.rawValue?.let { qrCode ->
                                        if (qrCode != qrCodeContent) {
                                            qrCodeContent = qrCode
                                            println("QR Code: $qrCode")
                                            checkinViewModel.checkin(memberId = qrCode)
                                        }
                                        imageProxy.close()
                                        return@addOnSuccessListener
                                    }
                                }
                                imageProxy.close()
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }
                    }
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        analysisUseCase
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
    if (isShowInfoDialog && attendanceState is DataState.Success) {
        val attendance = (attendanceState as DataState.Success).data
        BasicAlertDialog(
            modifier = Modifier
                .wrapContentSize()
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp),
            onDismissRequest = { isShowInfoDialog = false }) {
            Column(modifier = Modifier.fillMaxWidth()){
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    BasicText(text = buildAnnotatedString {
                        withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                            append("Name: ")
                        }
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(attendance.member!!.name)
                        }
                    }, style = MaterialTheme.typography.bodyMedium)
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(24.dp)
                            )
                            .background(
                                color = if (attendance.member!!.rank == "Basic") MaterialTheme.colorScheme.primaryContainer else GoldYellow.copy(
                                    alpha = 0.2f
                                )
                            ),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                            text = attendance.member!!.rank.toString(),
                            color = if (attendance.member!!.rank == "Basic") MaterialTheme.colorScheme.primary else Amber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                BasicText(text = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                        append("Email: ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(attendance.member!!.email)
                    }
                }, style = MaterialTheme.typography.bodyMedium)
                Gap.k8.Height()
                BasicText(text = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                        append("Phone: ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(attendance.member!!.phone)
                    }
                }, style = MaterialTheme.typography.bodyMedium)
                Gap.k8.Height()
                BasicText(text = buildAnnotatedString {
                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                        append("Status: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = if (attendance.member!!.status == "Active") ForestGreen else MaterialTheme.colorScheme.error
                        )
                    ) {
                        append(attendance.member!!.status)
                    }
                }, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}