package fptu.capstone.gymmanagesystemstaff.ui.equipment

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.TextField
import fptu.capstone.gymmanagesystemstaff.ui.theme.ForestGreen
import fptu.capstone.gymmanagesystemstaff.ui.theme.GoldYellow
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.utils.formatDateTime
import fptu.capstone.gymmanagesystemstaff.viewmodel.EquipmentViewModel

@Composable
fun SlotEquipmentDetailScreen(
    slotEquipmentId: String,
    equipmentViewModel: EquipmentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val slotEquipmentState by equipmentViewModel.slotEquipment.collectAsState()
    val progressEquipmentState by equipmentViewModel.progressEquipments.collectAsState()
    var rejectReason by remember { mutableStateOf("") }
    LaunchedEffect(progressEquipmentState) {
        equipmentViewModel.getSlotEquipmentById(slotEquipmentId)
        if (progressEquipmentState is DataState.Success) {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        } else if (progressEquipmentState is DataState.Error) {
            Toast.makeText(
                context,
                (progressEquipmentState as DataState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    when (slotEquipmentState) {
        is DataState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DataState.Success -> {
            val slotEquipment = (slotEquipmentState as DataState.Success).data
            Scaffold(
                floatingActionButton = {
                    if (slotEquipment.status == "Accept repay") return@Scaffold else Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp)
                    ) {
                        LargeButton(
                            modifier = Modifier.weight(1f),
                            text = "Accept",
                            isLoading = progressEquipmentState is DataState.Loading,
                        ) {
                            if (slotEquipment.status!!.lowercase().contains("borrow")) {
                                equipmentViewModel.acceptBorrow(slotEquipmentId)
                                println("slotEquipmentId: $slotEquipmentId")
                            } else {
                                equipmentViewModel.acceptRepay(slotEquipmentId)
                            }
                        }
                        Gap.k16.Width()
                        LargeButton(
                            modifier = Modifier.weight(1f),
                            text = "Reject",
                            isLoading = progressEquipmentState is DataState.Loading,
                            isAlter = true
                        ) {
                            if (rejectReason.isEmpty()) {
                                Toast.makeText(context, "Please enter reject reason", Toast.LENGTH_SHORT).show()
                            } else {
                                if (slotEquipment.status!!.lowercase().contains("borrow")) {
                                    equipmentViewModel.rejectBorrow(slotEquipmentId, rejectReason)
                                } else {
                                    equipmentViewModel.rejectRepay(slotEquipmentId, rejectReason)
                                }
                            }
                        }
                    }
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {
                        Gap.k16.Height()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(16.dp))
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .aspectRatio(1f),
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(slotEquipment.equipment?.thumbnailUrl).placeholder(
                                            R.drawable.placeholder
                                        ).error(R.drawable.error).build(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Equipment Image",
                                )
                                Gap.k16.Width()
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = slotEquipment.equipment?.name ?: "",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Gap.k8.Height()
                                    Text(
                                        text = slotEquipment.equipment?.description ?: "",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Gap.k8.Height()
                                    Text(
                                        text = slotEquipment.equipment?.status ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = if (slotEquipment.status == "Active") ForestGreen else Color.Gray
                                    )
                                }
                            }
                        }
                        Gap.k16.Height()
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(16.dp))
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                BasicText(text = buildAnnotatedString {
                                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                                        append("For: ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(slotEquipment.slot?.name ?: "")
                                    }
                                }, style = MaterialTheme.typography.bodyMedium)
                                Gap.k8.Height()
                                BasicText(text = buildAnnotatedString {
                                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                                        append("Created by: ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(slotEquipment.creator?.name ?: "")
                                    }
                                }, style = MaterialTheme.typography.bodyMedium)
                                Gap.k8.Height()
                                BasicText(text = buildAnnotatedString {
                                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                                        append("Created at: ")
                                    }
                                    slotEquipment.createAt.let {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(
                                                formatDateTime(
                                                    it.toString(),
                                                    "HH:mm dd/MM/yyyy"
                                                )
                                            )
                                        }
                                    }
                                }, style = MaterialTheme.typography.bodyMedium)
                                Gap.k8.Height()
                                BasicText(text = buildAnnotatedString {
                                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                                        append("From: ")
                                    }
                                    if (slotEquipment.slot != null) {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(
                                                formatDateTime(
                                                    slotEquipment.slot!!.startTime.toString(),
                                                    "HH:mm dd/MM/yyyy"
                                                )
                                            )
                                        }
                                    }
                                }, style = MaterialTheme.typography.bodyMedium)
                                Gap.k8.Height()
                                BasicText(text = buildAnnotatedString {
                                    withStyle(style = MaterialTheme.typography.titleSmall.toSpanStyle()) {
                                        append("To: ")
                                    }
                                    if (slotEquipment.slot != null) {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(
                                                formatDateTime(
                                                    slotEquipment.slot!!.endTime.toString(),
                                                    "HH:mm dd/MM/yyyy"
                                                )
                                            )
                                        }
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
                                            color = if (slotEquipment.status!!.contains("Accept")) ForestGreen else if (slotEquipment.status!!.contains(
                                                    "Reject"
                                                )
                                            ) MaterialTheme.colorScheme.error else GoldYellow
                                        )
                                    ) {
                                        append(slotEquipment.status ?: "")
                                    }
                                }, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Gap.k16.Height()
                        TextField(modifier = Modifier.fillMaxWidth(), onTextChange = { rejectReason = it }, label = "Reject reason", value = rejectReason, maxLines = 5)
                    }
                })
        }

        is DataState.Error -> {
            // Show error
        }

        else -> {}
    }


}