@file:OptIn(ExperimentalMaterial3Api::class)

package fptu.capstone.gymmanagesystemstaff.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fptu.capstone.gymmanagesystemstaff.R
import fptu.capstone.gymmanagesystemstaff.ui.component.Gap
import fptu.capstone.gymmanagesystemstaff.ui.component.LargeButton
import fptu.capstone.gymmanagesystemstaff.ui.component.TextField
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.viewmodel.ProfileViewModel

@Composable
fun ProfileDetailScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userId: String
) {
    val userState = profileViewModel.userState.collectAsState()
    LaunchedEffect(key1 = userId) {
        profileViewModel.getUserById(userId)
    }
    when (userState.value) {
        is DataState.Loading -> {
            CircularProgressIndicator(modifier = Modifier
                .padding(300.dp)
                .fillMaxSize())
        }

        is DataState.Error -> {
            Text(text = "Error: ${(userState.value as DataState.Error).message}")
        }

        is DataState.Success -> {
            val user = (userState.value as DataState.Success).data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    title = { /*TODO*/ },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "Update avatar"
                            )
                        }
                    })
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatarUrl)
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(75.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(75.dp))
                                .background(color = Color.Gray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.round_camera_alt_24),
                                contentDescription = "Update avatar"
                            )
                        }
                    }
                    Gap.k32.Height()
                    TextField(label = "Name", value = user.name, onTextChange = {})
                    Gap.k16.Height()
                    TextField(label = "Email", value = user.email, onTextChange = {})
                    Gap.k16.Height()
//                    TextField(label = "Phone", value = user.phone, onTextChange = {})
//                    Gap.k16.Height()
//                    TextField(
//                        label = "Address",
//                        value = "1 Lê Duẫn, phường Bến Nghé, quận 1, HCM",
//                        onTextChange = {})
                    Gap.k32.Height()
                    LargeButton(text = "Save", isLoading = false, onClick = {})
                    Gap.k16.Height()
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Change password")
                    }
                }
            }
        }

        else -> {
        }
    }
}