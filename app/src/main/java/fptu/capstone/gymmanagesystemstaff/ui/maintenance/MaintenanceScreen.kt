@file:OptIn(ExperimentalMaterial3Api::class)

package fptu.capstone.gymmanagesystemstaff.ui.maintenance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.model.Equipment
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.IconTextField
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.TextField
import fptu.capstone.gymmanagesystemstaff.ui.component.shimmerLoadingAnimation
import fptu.capstone.gymmanagesystemstaff.ui.maintenance.components.MaintainItem
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.EquipmentViewModel
import fptu.capstone.gymmanagesystemstaff.viewmodel.MaintennanceViewModel
import fptu.capstone.gymmanagesystemstaff.viewmodel.ProfileViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MaintenanceScreen(
    maintennanceViewModel: MaintennanceViewModel = hiltViewModel(),
    equipmentViewModel: EquipmentViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onMaintainClick: (maintainId: String) -> Unit,
) {
    val context = LocalContext.current
    val maintainsState by maintennanceViewModel.maintains.collectAsState()
    val equipmentsState by equipmentViewModel.equipments.collectAsState()
    val addMaintainState by maintennanceViewModel.addMaintain.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var searchText by remember { mutableStateOf("") }
    var isShowAddMaintainDialog by remember { mutableStateOf(false) }
    var selectedEquipmentId by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        maintennanceViewModel.fetchMaintains(FilterRequestBody(staffId = profileViewModel.getUser()!!.id))
        equipmentViewModel.fetchEquipments(FilterRequestBody())
    }
    LaunchedEffect(searchText) {
        maintennanceViewModel.fetchMaintains(
            FilterRequestBody(
                staffId = profileViewModel.getUser()!!.id,
                search = searchText
            )
        )
    }
    LaunchedEffect(addMaintainState) {
        if (addMaintainState is DataState.Success) {
            maintennanceViewModel.fetchMaintains(FilterRequestBody(staffId = profileViewModel.getUser()!!.id))
            Toast.makeText(
                context,
                "Add maintain successfully",
                Toast.LENGTH_SHORT
            ).show()
        } else if (addMaintainState is DataState.Error) {
            Toast.makeText(
                context,
                (addMaintainState as DataState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isShowAddMaintainDialog = true },
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
                    if (isShowAddMaintainDialog) {
                        BasicAlertDialog(
                            modifier = Modifier.wrapContentSize().clip(shape = RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.background),
                            onDismissRequest = { isShowAddMaintainDialog = false },
                            content = {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Text(text = "Add Maintain", style = MaterialTheme.typography.titleLarge)
                                    Gap.k16.Height()
                                    if (equipmentsState is DataState.Success) {
                                        SelectionBox (equipments = (equipmentsState as DataState.Success).data.data.filter { e -> e.status == "Active" }, onItemSelected = {
                                            selectedEquipmentId = it
                                        })
                                    }
                                    Gap.k16.Height()
                                    TextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = description,
                                        label = "Description",
                                        onTextChange = {
                                            description = it
                                        },
                                        maxLines = 5
                                    )
                                    Gap.k16.Height()
                                    LargeButton(isLoading = addMaintainState is DataState.Loading, text = "Add Maintain", enabled = selectedEquipmentId != null) {
                                        maintennanceViewModel.addMaintain(staffId = profileViewModel.getUser()!!.id!!, equipmentId = selectedEquipmentId!!, description = description, date = ZonedDateTime.now().format(
                                            DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                        if (addMaintainState !is DataState.Loading) {
                                            isShowAddMaintainDialog = false
                                        }
                                    }
                                }
                            })
                    }
                }
            }
        })
}

@Composable
fun SelectionBox(onItemSelected: (String) -> Unit, equipments: List<Equipment>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf<Equipment?>(null) }

    Box(modifier = Modifier.fillMaxWidth().clip(shape = RoundedCornerShape(16.dp)).background(color = MaterialTheme.colorScheme.secondaryContainer) ) {
        Text(
            text = if(selectedOption != null) selectedOption!!.name!! else "Select an equipment",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                }
                .padding(16.dp)
        )

        DropdownMenu(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            equipments.forEach { option ->
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onItemSelected(option.id!!)
                            selectedOption = option
                            expanded = false
                        },
                    text = option.name!!
                )
            }

        }
    }
}