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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.CheckinViewModel

@ExperimentalGetImage
@Composable
fun CheckinScreen(checkinViewModel: CheckinViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var qrCodeContent by remember { mutableStateOf("") }
    val previewView = remember { PreviewView(context) }
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val attendanceState by checkinViewModel.checkinState.collectAsState()

    LaunchedEffect(attendanceState) {
        if (attendanceState is DataState.Success) {
            val attendance = (attendanceState as DataState.Success).data
            Toast.makeText(context, "Check-in success", Toast.LENGTH_SHORT).show()
        } else if (attendanceState is DataState.Error) {
            qrCodeContent = ""
            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show()
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
                    ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                analysisUseCase.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
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
}