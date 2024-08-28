package fptu.capstone.gymmanagesystemstaff.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fptu.capstone.gymmanagesystemstaff.model.Equipment
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.model.Response
import fptu.capstone.gymmanagesystemstaff.model.SlotEquipment
import fptu.capstone.gymmanagesystemstaff.network.RejectBody
import fptu.capstone.gymmanagesystemstaff.repositories.EquipmentRepository
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.utils.ErrorHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EquipmentViewModel @Inject constructor(private val equipmentRepository: EquipmentRepository) : ViewModel() {
    private val _equipmens = MutableStateFlow<DataState<Response<Equipment>>>(DataState.Idle)
    val equipments : StateFlow<DataState<Response<Equipment>>> = _equipmens
    private val _equipmentsRequested = MutableStateFlow<DataState<List<SlotEquipment>>>(DataState.Idle)
    val equipmentsRequested : StateFlow<DataState<List<SlotEquipment>>> = _equipmentsRequested
    private val _slotEquipments = MutableStateFlow<DataState<Response<SlotEquipment>>>(DataState.Idle)
    val slotEquipments : StateFlow<DataState<Response<SlotEquipment>>> = _slotEquipments
    private val _slotEquipment = MutableStateFlow<DataState<SlotEquipment>>(DataState.Idle)
    val slotEquipment : StateFlow<DataState<SlotEquipment>> = _slotEquipment
    private val _progressEquipments = MutableStateFlow<DataState<SlotEquipment>>(DataState.Idle)
    val progressEquipments : StateFlow<DataState<SlotEquipment>> = _progressEquipments

    fun fetchEquipments(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _equipmens.value = DataState.Loading
            try {
                val response = equipmentRepository.getEquipments(filterRequestBody = filterRequestBody)
                _equipmens.value = DataState.Success(response)
            } catch (e: HttpException) {
                _equipmens.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun fetchSlotEquipments(filterRequestBody: FilterRequestBody) {
        viewModelScope.launch {
            _slotEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.getSlotEquipments(filterRequestBody = filterRequestBody)
                _slotEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                _slotEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun deleteSlotEquipment(id: String) {
        viewModelScope.launch {
            _slotEquipment.value = DataState.Loading
            try {
                val response = equipmentRepository.deleteSlotEquipment(id = id)
                _slotEquipment.value = DataState.Success(response)
            } catch (e: HttpException) {
                _slotEquipment.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun getSlotEquipmentById(id: String){
        viewModelScope.launch {
            _slotEquipment.value = DataState.Loading
            try {
                val response = equipmentRepository.getSlotEquipmentById(id = id)
                _slotEquipment.value = DataState.Success(response)
            } catch (e: HttpException) {
                _slotEquipment.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun acceptBorrow(id: String){
        viewModelScope.launch {
            _progressEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.acceptBorrow(id = id)
                _progressEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                println("acceptBorrow: $e")
                _progressEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun acceptRepay(id: String){
        viewModelScope.launch {
            _progressEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.acceptRepay(id = id)
                _progressEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                _progressEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun rejectBorrow(id: String, reason: String){
        viewModelScope.launch {
            _progressEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.rejectBorrow(id = id, body = RejectBody(reason = reason))
                _progressEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                _progressEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }

    fun rejectRepay(id: String, reason: String){
        viewModelScope.launch {
            _progressEquipments.value = DataState.Loading
            try {
                val response = equipmentRepository.rejectRepay(id = id, body = RejectBody(reason = reason))
                _progressEquipments.value = DataState.Success(response)
            } catch (e: HttpException) {
                _progressEquipments.value = DataState.Error(ErrorHandler(e.code()))
            }
        }
    }
}