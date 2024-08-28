package fptu.capstone.gymmanagesystemstaff.ui.equipment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.IconTextField
import fptu.capstone.gymmanagesystemstaff.ui.equipment.component.RequestItem
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.EquipmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotEquipmentScreen(equipmentViewModel: EquipmentViewModel = hiltViewModel(), onDetailClick: (String) -> Unit) {
    var isRefreshing by remember { mutableStateOf(false) }
    val slotEquipmentsState by equipmentViewModel.slotEquipments.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        equipmentViewModel.fetchSlotEquipments(FilterRequestBody(search = searchText))
    }

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        isRefreshing = true
        // fetch data
        isRefreshing = false
    }) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Gap.k16.Height()
            IconTextField(
                value = searchText,
                placeholder = "Search equipment",
                onValueChange = { searchText = it })
            Gap.k16.Height()
            when (slotEquipmentsState) {
                is DataState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is DataState.Success -> {
                    val slotEquipments =
                        (slotEquipmentsState as DataState.Success).data.data
                    if (slotEquipments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No equipment found",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else
                        slotEquipments.forEach { se ->
                            val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart) {
                                        equipmentViewModel.deleteSlotEquipment(se.id!!)
                                    }
                                    true
                                }
                            )
                            SwipeToDismissBox(state = swipeToDismissBoxState,
                                enableDismissFromEndToStart = true,
                                enableDismissFromStartToEnd = false,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.round_delete_24),
                                            contentDescription = "Delete Inquiry",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                content = {
                                    RequestItem(se){
                                        onDetailClick(se.id!!)
                                    }
                                })
                            Gap.k16.Height()
                        }
                }

                is DataState.Error -> {
                    val error = (slotEquipmentsState as DataState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                else -> {}
            }
        }
    }
}