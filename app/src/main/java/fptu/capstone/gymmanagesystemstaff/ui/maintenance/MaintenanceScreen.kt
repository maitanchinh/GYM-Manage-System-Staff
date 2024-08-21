package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.IconTextField
import fptu.capstone.gymmanagesystemstaff.ui.component.shimmerLoadingAnimation
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.MaintainItem
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel
import fptu.capstone.gymmanagesystemstaff.viewmodel.ProfileViewModel

@Composable
fun MaintenanceScreen(
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onMaintainClick: (maintainId: String) -> Unit,
    onAddClick: () -> Unit
) {
    val maintainsState by maintennanceViewModel.maintains.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var searchText by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        maintennanceViewModel.fetchMaintains(FilterRequestBody(staffId = profileViewModel.getUser()!!.id))
    }
    LaunchedEffect(searchText) {
        maintennanceViewModel.fetchMaintains(
            FilterRequestBody(
                staffId = profileViewModel.getUser()!!.id,
                search = searchText
            )
        )
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_add_24),
                    contentDescription = "Add maintain"
                )
            }
        },
        content = { paddingValues ->
            SwipeRefresh(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = swipeRefreshState, onRefresh = {
                    isRefreshing = true
                    maintennanceViewModel.fetchMaintains(FilterRequestBody(staffId = profileViewModel.getUser()!!.id))
                    isRefreshing = false
                }) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(state = rememberScrollState())
                ) {
                    IconTextField(
                        value = searchText,
                        placeholder = "Search",
                        onValueChange = { searchText = it })
                    Gap.k16.Height()
                    when (maintainsState) {
                        is DataState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(shape = RoundedCornerShape(20.dp))
                                        .shimmerLoadingAnimation()
                                )
                            }
                        }

                        is DataState.Success -> {
                            val maintains = (maintainsState as DataState.Success).data.data
                            if (maintains.isEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = "No data",
                                    textAlign = TextAlign.Center
                                )
                            } else

                                maintains.let {
                                    maintains.forEach { m ->
                                        MaintainItem(maintain = m) {
                                            onMaintainClick(m.id!!)
                                        }
                                    }
                                }
                        }

                        is DataState.Error -> {
                            val errorMessage = (maintainsState as DataState.Error).message
                            Text(text = errorMessage)
                        }

                        else -> {}
                    }
                }
            }
        })
}