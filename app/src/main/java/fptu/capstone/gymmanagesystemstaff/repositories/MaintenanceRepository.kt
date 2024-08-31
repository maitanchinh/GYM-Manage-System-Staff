package fptu.capstone.gymmanagesystemstaff.repositories

import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.model.Maintain
import fptu.capstone.gymmanagesystemstaff.model.MaintainPickup
import fptu.capstone.gymmanagesystemstaff.model.MaintainResult
import fptu.capstone.gymmanagesystemstaff.network.MaintenanceApiService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MaintenanceRepository @Inject constructor(private val maintenanceApiService: MaintenanceApiService) {
    suspend fun getMaintains(filterRequestBody: FilterRequestBody) = maintenanceApiService.getMaintains(filterRequestBody)
    suspend fun getMaintainPickups(filterRequestBody: FilterRequestBody) = maintenanceApiService.getMaintainPickups(filterRequestBody)
    suspend fun getMaintainResults(filterRequestBody: FilterRequestBody) = maintenanceApiService.getMaintainResults(filterRequestBody)
    suspend fun getMaintainResultById(id: String) = maintenanceApiService.getMaintainResultById(id)
    suspend fun addMaintain(staffId: String, equipmentId: String, description: String, date: String) : Maintain{
        val staffIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), staffId)
        val equipmentIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), equipmentId)
        val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
        val dateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), date)
        return maintenanceApiService.addMaintain(staffIdBody, equipmentIdBody, descriptionBody, dateBody)
    }
    suspend fun addMaintainPickup(maintainId: String, image: File) : MaintainPickup{
        val maintainIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), maintainId)
        val imagePart = image.let {
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }
        return maintenanceApiService.addMaintainPickup(maintainIdBody, imagePart)
    }
    suspend fun addMaintainResult(maintainId: String, image: File, date: String, cost: Double, invoiceImage: File?, isFixed: Boolean) : MaintainResult {
        val maintainIdBody = maintainId.toRequestBody("text/plain".toMediaTypeOrNull())
        val dateBody = date.toRequestBody("text/plain".toMediaTypeOrNull())
        val imagePart = image.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }
        val invoiceImagePart = invoiceImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("invoiceImage", it.name, requestFile)
        }
        return maintenanceApiService.addMaintainResult(maintainId = maintainIdBody, image = imagePart, date = dateBody, cost = cost, invoiceImage = invoiceImagePart, isFixed = isFixed)
    }
    suspend fun deleteMaintainResult(id: String) = maintenanceApiService.deleteMaintainResult(id)
    suspend fun deleteMaintainPickup(id: String) = maintenanceApiService.deleteMaintainPickup(id)
}