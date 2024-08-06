package fptu.capstone.gymmanagesystemstaff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fptu.capstone.gymmanagesystemstaff.model.Attendance
import fptu.capstone.gymmanagesystemstaff.repositories.AttendanceRepository
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(private val attendanceRepository: AttendanceRepository) : ViewModel() {
    private val _trainerAttendance = MutableStateFlow<DataState<Attendance>>(DataState.Idle)
    val trainerAttendance : MutableStateFlow<DataState<Attendance>> = _trainerAttendance

    fun createAttendance(trainerId: String, slotId: String) {
        viewModelScope.launch {
            _trainerAttendance.value = DataState.Loading
            try {
                val attendance: Attendance = attendanceRepository.createAttendance(trainerId = trainerId, slotId = slotId)
                _trainerAttendance.value = DataState.Success(attendance)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error at createAttendance: ${e.message}")
                _trainerAttendance.value = DataState.Error(e.message ?: "Unknown error")
            }
        }
    }
}