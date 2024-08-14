package fptu.capstone.gymmanagesystemstaff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fptu.capstone.gymmanagesystemstaff.model.Checkin
import fptu.capstone.gymmanagesystemstaff.repositories.CheckinRepository
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckinViewModel @Inject constructor(private val checkinRepository: CheckinRepository) :ViewModel() {
    private val _checkinState = MutableStateFlow<DataState<Checkin>>(DataState.Idle)
    val checkinState : MutableStateFlow<DataState<Checkin>> = _checkinState

    fun checkin(memberId: String) {
        viewModelScope.launch {
            _checkinState.value = DataState.Loading
            try {
                val response = checkinRepository.checkin(memberId)
                _checkinState.value = DataState.Success(response)
            } catch (e: Exception) {
                e.printStackTrace()
                _checkinState.value = DataState.Error("Checkin failed")
            }
        }
    }
}