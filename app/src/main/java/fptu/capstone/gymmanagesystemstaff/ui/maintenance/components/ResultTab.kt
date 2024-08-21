package fptu.capstone.gymmanagesystemstaff.ui.maintenance.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.shimmerLoadingAnimation
import fptu.capstone.gymmanagesystemstaff.ui.navigation.Route
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel

@Composable
fun ResultTab(
    maintainId: String,
    navController: NavController,
    onResultClick: (resultId: String) -> Unit,
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel()
) {
    val resultsState by maintennanceViewModel.results.collectAsState()
    val resultState by maintennanceViewModel.result.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    var isDeleteDialogShow by remember { mutableStateOf(false) }

    LaunchedEffect(currentBackStackEntry.value) {
        // Reload dữ liệu khi màn hình trở thành màn hình hiện tại
        if (currentBackStackEntry.value?.destination?.route == Route.Maintain.route) {
            maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
        }
    }
    LaunchedEffect(resultState) {
        if (resultState is DataState.Success) {
            Toast.makeText(navController.context, "Delete success", Toast.LENGTH_SHORT).show()
            maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
        } else if (resultState is DataState.Error) {
            Toast.makeText(
                navController.context,
                (resultState as DataState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    LaunchedEffect(Unit) {
        maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
    }
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
            isRefreshing = true
            maintennanceViewModel.fetchResults(FilterRequestBody(maintainId = maintainId))
            isRefreshing = false
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
        ) {
            when (resultsState) {
                is DataState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .shimmerLoadingAnimation()
                    ) {

                    }
                }

                is DataState.Success -> {
                    val results = (resultsState as DataState.Success).data.data
                    if (results.isEmpty()) {
                        Text(text = "No result found")
                    } else {
                        Text(
                            text = "Result",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Gap.k16.Height()
                        ResultItem(result = results.first(), onCLick = {
                            onResultClick(results.first().id!!)
                        }, onDeleteCLick = {
                            isDeleteDialogShow = true
                        })
                    }
                }

                is DataState.Error -> {
                    Text(text = (resultsState as DataState.Error).message)
                }

                else -> {}
            }
            if (isDeleteDialogShow) {
                AlertDialog(onDismissRequest = { isDeleteDialogShow = false }, confirmButton = {
                    TextButton(onClick = {
                        maintennanceViewModel.deleteMaintainResult((resultsState as DataState.Success).data.data.first().id!!)
                        isDeleteDialogShow = false
                    }) {
                        Text(text = "Yes")
                    }
                },
                    dismissButton = {
                        TextButton(onClick = { isDeleteDialogShow = false }) {
                            Text(text = "No")
                        }
                    },
                    text = {
                        Text(text = "Do you want to delete this result?")
                    })
            }
        }
    }
}