package fptu.capstone.gymmanagesystemstaff.ui.gymclass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.ClassViewModel

@Composable
fun AllClassScreen(viewModel: ClassViewModel = hiltViewModel(), onClassClick: (id: String) -> Unit = {}) {
    val classes = viewModel.classes.collectAsState()
    val category = listOf("All", "Cardio", "Strength", "Yoga", "Dance", "Boxing", "Pilates")
    val selectedCategory = remember { mutableStateOf("All") }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val items = (classes.value as? DataState.Success)?.data?.classes ?: emptyList()
//        val items = classes.value.classes
        item(span = { GridItemSpan(2) }) {
            Column {
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
        items(items.size) { index ->
            ClassCard(items[index], onClassClick = onClassClick)
        }
    }
}