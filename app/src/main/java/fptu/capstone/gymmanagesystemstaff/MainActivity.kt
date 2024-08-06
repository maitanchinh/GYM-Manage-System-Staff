package fptu.capstone.gymmanagesystemstaff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fptu.capstone.gymmanagesystemstaff.ui.navigation.AppNavigation
import fptu.capstone.gymmanagesystemstaff.ui.theme.GYMManageSystemTheme
import fptu.capstone.gymmanagesystemstaff.viewmodel.ProfileViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val isDarkMode by profileViewModel.darkMode.observeAsState(false)
            GYMManageSystemTheme(darkTheme = isDarkMode) {
                val navHostController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        modifier = Modifier.fillMaxSize(),
                        navHostController = navHostController,
                    )
                }
            }
        }
    }
}
