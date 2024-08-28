package fptu.capstone.gymmanagesystemstaff.repositories

import fptu.capstone.gymmanagesystemstaff.model.FilterRequestBody
import fptu.capstone.gymmanagesystemstaff.network.EquipmentApiService
import fptu.capstone.gymmanagesystemstaff.network.RejectBody
import javax.inject.Inject

class EquipmentRepository @Inject constructor(private val equipmentApiService: EquipmentApiService) {
    suspend fun getEquipments(filterRequestBody: FilterRequestBody) = equipmentApiService.getEquipments(filterRequestBody)
    suspend fun getSlotEquipments(filterRequestBody: FilterRequestBody) = equipmentApiService.getSlotEquipments(filterRequestBody)
    suspend fun deleteSlotEquipment(id: String) = equipmentApiService.deleteSlotEquipment(id)
    suspend fun getSlotEquipmentById(id: String) = equipmentApiService.getSlotEquipmentById(id)
    suspend fun acceptBorrow(id: String) = equipmentApiService.acceptBorrow(id)
    suspend fun acceptRepay(id: String) = equipmentApiService.acceptRepay(id)
    suspend fun rejectBorrow(id: String, body: RejectBody) = equipmentApiService.rejectBorrow(id, body)
    suspend fun rejectRepay(id: String, body: RejectBody) = equipmentApiService.rejectRepay(id, body)
}