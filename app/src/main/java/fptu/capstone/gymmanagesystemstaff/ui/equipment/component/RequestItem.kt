package fptu.capstone.gymmanagesystemstaff.ui.equipment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.SlotEquipment
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.theme.ForestGreen
import fptu.capstone.gymmanagesystemstaff.ui.theme.GoldYellow

@Composable
fun RequestItem(it: SlotEquipment, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(it.equipment?.thumbnailUrl).placeholder(
                        R.drawable.placeholder
                    ).error(R.drawable.error).build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(70.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Gap.k16.Width()
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = it.equipment!!.name.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Requested by: " + it.creator?.name.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "For: " + it.slot?.name.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = it.status.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (it.status!!.contains("Accept")) ForestGreen else if (it.status!!.contains(
                            "Reject"
                        )
                    ) MaterialTheme.colorScheme.error else GoldYellow
                )
            }
        }
    }
}