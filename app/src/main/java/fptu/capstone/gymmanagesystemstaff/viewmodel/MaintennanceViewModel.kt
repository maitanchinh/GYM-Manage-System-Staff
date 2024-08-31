package fptu.capstone.gymmanagesystemstaff.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.model.Maintain
import fptu.capstone.gymmanagesystemstaff.model.MaintainPickup
import fptu.capstone.gymmanagesystemstaff.model.MaintainResult
import fptu.capstone.gymmanagesystemstaff.model.Response
import fptu.capstone.gymmanagesystemstaff.repositories.MaintenanceRepository
import fptu.capstone.gymmanagesystemstaff.utils.DataState
import fptu.capstone.gymmanagesystemstaff.utils.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MaintennanceViewModel @Inject constructor(private val maintenanceRepository: MaintenanceRepository) : ViewModel(){
    private val _maintains = MutableStateFlow<DataState<Response<Maintain>>>(DataState.Idle)
    val maintains : StateFlow<DataState<Response<Maintain>>> = _maintains
    private val _pickups = MutableStateFlow<DataState<Response<MaintainPickup>>>(DataState.Idle)
    val pickups : StateFlow<DataState<Response<MaintainPickup>>> = _pickups
    private val _pickup = MutableStateFlow<DataState<MaintainPickup>>(DataState.Idle)
    val pickup : StateFlow<DataState<MaintainPickup>> = _pickup
    private val _results = MutableStateFlow<DataState<Response<MaintainResult>>>(DataState.Idle)
    val results : StateFlow<DataState<Response<MaintainResult>>> = _results
    private val _result = MutableStateFlow<DataState<MaintainResult>>(DataState.Idle)
    val result : StateFlow<DataState<MaintainResult>> = _result
    private val _addMaintain = MutableStateFlow<DataState<Maintain>>(DataState.Idle)
    val addMaintain : StateFlow<DataState<Maintain>> = _addMaintain
    private val _pickupImage = MutableStateFlow<File?>(null)
    val pickupImage: StateFlow<File?> = _pickupImage
    private val _imageBitmap = MutableStateFlow<Bitmap?>(null)
    val pickupImageBitmap: StateFlow<Bitmap?> = _imageBitmap
    private val _resultImage = MutableStateFlow<File?>(null)
    val resultImage: StateFlow<File?> = _resultImage
    private val _resultImageBitmap = MutableStateFlow<Bitmap?>(null)
    val resultImageBitmap: StateFlow<Bitmap?> = _resultImageBitmap
    private val _invoiceImage = MutableStateFlow<File?>(null)
    val invoiceImage: StateFlow<File?> = _invoiceImage
    private val _invoiceImageBitmap = MutableStateFlow<Bitmap?>(null)
    val invoiceImageBitmap: StateFlow<Bitmap?> = _invoiceImageBitmap
    private val _addPickup = MutableStateFlow<DataState<MaintainPickup>>(DataState.Idle)
    val addPickup : StateFlow<DataState<MaintainPickup>> = _addPickup
    private val _date = MutableStateFlow<String>("")
    val date : StateFlow<String> = _date
    private val _cost = MutableStateFlow<String?>(null)
    val cost : StateFlow<String?> = _cost
    private val _fixedStatus = MutableStateFlow<Boolean?>(null)
    val fixedStatus : StateFlow<Boolean?> = _fixedStatus

    fun setPickupImage(image: File?) {
        _pickupImage.value = image
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        _imageBitmap.value = bitmap
    }

    fun setResultImage(image: File?) {
        _resultImage.value = image
    }

    fun setResultImageBitmap(bitmap: Bitmap?) {
        _resultImageBitmap.value = bitmap
    }

    fun setInvoiceImage(image: File?) {
        _invoiceImage.value = image
    }

    fun setInvoiceImageBitmap(bitmap: Bitmap?) {
        _invoiceImageBitmap.value = bitmap
    }

    fun onDateChange(date: String) {
        _date.value = date
    }

    fun onCostChange(cost: String) {
        _cost.value = cost
    }

    fun setFixedStatus(fixedStatus: Boolean) {
        _fixedStatus.value = fixedStatus
        println(_fixedStatus.value)
    }

    fun fetchMaintains(filterRequestBody: FilterRequestBody){
        viewModelScope.launch {
            _maintains.value = DataState.Loading
            try {
                val response: Response<Maintain> = maintenanceRepository.getMaintains(filterRequestBody = filterRequestBody)
                _maintains.value = DataState.Success(response)
                println(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _maintains.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }
    fun fetchPickups(filterRequestBody: FilterRequestBody){
        viewModelScope.launch {
            _pickups.value = DataState.Loading
            try {
                val response: Response<MaintainPickup> = maintenanceRepository.getMaintainPickups(filterRequestBody = filterRequestBody)
                _pickups.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                println(e.code())
                _pickups.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }

    fun fetchResults(filterRequestBody: FilterRequestBody){
        viewModelScope.launch {
            _results.value = DataState.Loading
            try {
                val response: Response<MaintainResult> = maintenanceRepository.getMaintainResults(filterRequestBody = filterRequestBody)
                _results.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _results.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }

    fun getResultById(id: String){
        viewModelScope.launch {
            _result.value = DataState.Loading
            try {
                val response: MaintainResult = maintenanceRepository.getMaintainResultById(id = id)
                _result.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _result.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }

    fun addMaintain(staffId: String, equipmentId: String, description: String, date: String){
        viewModelScope.launch {
            _addMaintain.value = DataState.Loading
            try {
                val response: Maintain = maintenanceRepository.addMaintain(staffId = staffId, equipmentId = equipmentId, description = description, date = date)
                _addMaintain.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _addMaintain.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }

    fun addMaintainPickup(maintainId: String){
        println("image: ${_pickupImage.value}")
        viewModelScope.launch {
            _addPickup.value = DataState.Loading
            try {
                val response: MaintainPickup = maintenanceRepository.addMaintainPickup(maintainId = maintainId, image = _pickupImage.value!!)
                _addPickup.value = DataState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                _addPickup.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else if (e.message() == "DUPLICATE RECORD") "Pickup already exists" else "Error")
            } finally {
                _pickupImage.value = null
                _imageBitmap.value = null
            }
        }
    }

    fun addMaintainResult(maintainId: String){
        viewModelScope.launch {
            _result.value = DataState.Loading
            try {
                val response: MaintainResult = maintenanceRepository.addMaintainResult(maintainId = maintainId, image = _resultImage.value!!, date = _date.value, cost = _cost.value!!.toDouble(), invoiceImage = _invoiceImage.value, isFixed = _fixedStatus.value!!)
                _result.value = DataState.Success(response)
            } catch (e: HttpException) {
                println(e)
                e.printStackTrace()
                _result.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else if (e.message() == "DUPLICATE RECORD") "Result already exists" else "Error")
            } finally {
                _invoiceImage.value = null
                _invoiceImageBitmap.value = null
            }
        }
    }

    fun deleteMaintainPickup(id: String){
        println(id)
        _pickup.value = DataState.Loading
        viewModelScope.launch {
            try {
                _pickup.value = DataState.Success(maintenanceRepository.deleteMaintainPickup(id))
            } catch (e: HttpException) {
                e.printStackTrace()
                _pickup.value = DataState.Error(if (e.code() == 400) "Maintain is processing, cannot delete pickup" else Message.FETCH_DATA_FAILURE.message)
            }
        }
    }

    fun deleteMaintainResult(id: String){
        _result.value = DataState.Loading
        viewModelScope.launch {
            try {
                _result.value = DataState.Success(maintenanceRepository.deleteMaintainResult(id))
            } catch (e: HttpException) {
                e.printStackTrace()
                _result.value = DataState.Error(if (e.code() != 200) Message.FETCH_DATA_FAILURE.message else "Error")
            }
        }
    }
}