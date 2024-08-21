package fptu.capstone.gymmanagesystemstaff.ui.maintenance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.Maintain
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.theme.ForestGreen
import fptu.capstone.gymmanagesystemstaff.ui.theme.GoldYellow

@Composable
fun MaintainItem(maintain: Maintain, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(maintain.equipment?.thumbnailUrl).placeholder(R.drawable.placeholder)
                    .error(R.drawable.error).build(),
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
                contentDescription = "Equipment Image",
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = maintain.equipment?.name!!, style = MaterialTheme.typography.titleLarge)
                    Gap.k8.Height()
                    maintain.description.let {
                        Text(text = it.toString())
                    }
                }
                Box(modifier = Modifier.padding(16.dp).clip(shape = RoundedCornerShape(16.dp)).background(if (maintain.status == "Completed") ForestGreen else GoldYellow)) {
                    Text(
                        text = maintain.status.toString(),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}