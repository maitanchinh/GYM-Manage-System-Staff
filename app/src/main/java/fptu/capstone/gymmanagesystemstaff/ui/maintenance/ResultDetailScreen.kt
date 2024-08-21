package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.theme.ForestGreen
import fptu.capstone.gymmanagesystemstaff.ui.theme.GoldYellow
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.utils.parseDateTime
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ResultDetailScreen(
    resultId: String,
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel()
) {
    val resultState by maintennanceViewModel.result.collectAsState()

    LaunchedEffect(Unit) {
        maintennanceViewModel.getResultById(resultId)
    }
    println(resultState)
    when (resultState) {
        is DataState.Loading -> {
            CircularProgressIndicator()
        }

        is DataState.Success -> {
            val result = (resultState as DataState.Success).data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(result.image)
                        .placeholder(R.drawable.placeholder).error(R.drawable.error).build(),
                    contentDescription = "image",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
                Gap.k32.Height()
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.round_payments_32), contentDescription = null, tint = Color.Gray)
                        Gap.k16.Width()
                        Text(text = NumberFormat.getNumberInstance(Locale.getDefault()).format(result.cost) + " VND", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(if (result.status == "Success") ForestGreen else GoldYellow)
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Text(text = result.status!!, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSecondary, fontWeight = FontWeight.Bold)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.round_access_time_32), contentDescription = null, tint = Color.Gray)
                    result.date?.let { parseDateTime(it) }
                        ?.let {
                            Text(
                                modifier = Modifier.padding(16.dp), text = it.format(
                                    DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")
                                ), style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Left
                            )
                        }

                }
                Gap.k32.Height()
                if (result.invoiceImage != null) {
                    Text(
                        text = "Invoice image",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Gap.k16.Height()
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(result.invoiceImage)
                            .placeholder(R.drawable.placeholder).error(R.drawable.error).build(),
                        contentDescription = "image",
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        is DataState.Error -> {
            val error = (resultState as DataState.Error).message
            Text(text = error)
        }

        else -> {}
    }
}