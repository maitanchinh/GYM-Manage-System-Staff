@file:OptIn(ExperimentalMaterial3Api::class)

package fptu.capstone.gymmanagesystemstaff.ui.gymclass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.IconTextField
import fptu.capstone.gymmanagesystemstaff.ui.theme.Purple40
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.ClassViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassScreen(
    onViewAllClassClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onClassClick: (id: String) -> Unit = {},
    onViewAllMyClassClick: () -> Unit = {},
    viewModel: ClassViewModel = hiltViewModel()
) {
    val classes by viewModel.classes.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val notificationCount by remember { mutableStateOf(2) }
    val category = listOf("All", "Cardio", "Strength", "Yoga", "Dance", "Boxing", "Pilates")
    val selectedCategory = remember { mutableStateOf("All") }
    var isClassesRefreshing by remember { mutableStateOf(false) }
//    val swipeRefreshState by remember { mutableStateOf(SwipeRefreshState(isRefreshing = classes is DataState.Loading)) }
    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing = isClassesRefreshing),
//        state = swipeRefreshState,
        onRefresh = {
            isClassesRefreshing = true
            viewModel.refreshClasses()
            isClassesRefreshing = false
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Row {
                Image(
                    painter = rememberAsyncImagePainter(model = "https://avatar.iran.liara.run/public/48"),
                    contentDescription = "My Class",
                    modifier = Modifier
                        .size(40.dp)
                        .clipToBounds()
                )
                Gap.k16.Width()
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Hello",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                    )
                    //                Gap.k8.Height()
                    Text(
                        text = "Nguyễn Văn An",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Box(modifier = Modifier.size(40.dp)) {
                    IconButton(onClick = { /*TODO*/ }) {
                        BadgedBox(badge = {
                            if (notificationCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White,
                                    modifier = Modifier.offset(x = (-10).dp, y = 10.dp)
                                ) {
                                    Text(text = notificationCount.toString())
                                }
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.round_notifications_32),
                                contentDescription = "Notification",
                                tint = Color(0xFFED9455)
                            )
                        }
                    }
                }
            }
            Gap.k16.Height()

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        IconTextField(
                            value = searchText,
                            placeholder = "Search class",
                            onValueChange = { searchText = it })
                        //        Gap.k16.Height()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Class",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                            )
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "View all")
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(20))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = "https://thumbs.dreamstime.com/b/aerobic-workout-logo-fitness-exercise-gym-vector-set-white-background-80759379.jpg"),
                                    contentDescription = "Class Thumbnail",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(shape = RoundedCornerShape(20))
                                )
                                Gap.k16.Width()
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Cardio And Strength",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Gap.k8.Height()
                                    Text(
                                        text = "Everyday 7:00 - 8:00",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val size = size.minDimension
                                        val stroke = Stroke(width = 15f)
                                        val startAngle = -90f
                                        val sweepAngle: Float = (360 * 69 / 100).toFloat()

                                        // Draw background circle
                                        drawCircle(
                                            color = Color.White,
                                            radius = size / 2,
                                            style = stroke
                                        )

                                        // Draw progress arc
                                        drawArc(
                                            color = Purple40,
                                            startAngle = startAngle,
                                            sweepAngle = sweepAngle,
                                            useCenter = false,
                                            style = stroke,
                                            size = Size(size, size),
                                            topLeft = Offset(0f, 0f)
                                        )
                                    }

                                    // Draw percentage text
                                    Text(
                                        text = "${69.toInt()}%",
                                        fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        //        Gap.k16.Height()
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Suggestion",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = { onViewAllMyClassClick() }) {
                                Text(text = "View all")
                            }
                        }
                        FlowRow(mainAxisSpacing = 16.dp, crossAxisSpacing = 8.dp) {
                            category.forEach {
                                val isSelected = it == selectedCategory.value
                                val backgroundColor =
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                                Box(
                                    modifier = Modifier
                                        .clip(shape = RoundedCornerShape(50))
                                        .background(backgroundColor)
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .clickable { selectedCategory.value = it }
                                ) {
                                    Text(text = it)
                                }
                            }
                        }
                    }
                }
                when (classes) {
                    is DataState.Loading -> {
                        item(span = { GridItemSpan(2) }) {
                            CircularProgressIndicator(modifier = Modifier.padding(180.dp))
                        }
                    }

                    is DataState.Error -> {
                        item {
                            Text(
                                text = "Error loading classes",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    is DataState.Success -> {
                        val items = (classes as? DataState.Success)?.data?.classes ?: emptyList()
                        items(items.size) { index ->
                            ClassCard(gymClass = items[index], onClassClick = onClassClick)
                        }
                    }

                    is DataState.Empty -> {
                        item {
                            Text(
                                text = "No classes found",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    else -> {
                    }
                }
//                val items = classes.classes
//                        items(items.size) { index ->
//                            ClassCard(gymClass = items[index], onClassClick = onClassClick)
//                        }
            }
        }
    }
}


