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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.MaintainResult
import fptu.capstone.gymmanagesystemstaff.utils.parseDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ResultItem(
    result: MaintainResult,
    onCLick: () -> Unit,
    onDeleteCLick: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(20.dp))
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .clickable { onCLick() }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(result.image)
                    .placeholder(R.drawable.placeholder).error(R.drawable.error).build(),
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                result.date?.let { parseDateTime(it) }
                    ?.let {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = it.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
                        .clickable { onDeleteCLick() }
                        .padding(8.dp)

                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_delete_outline_24),
                        contentDescription = "delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
    }
}