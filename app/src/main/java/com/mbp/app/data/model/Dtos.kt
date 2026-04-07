package com.mbp.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(val email: String, val password: String)

@JsonClass(generateAdapter = true)
data class PhoneLoginRequest(val phone: String)

@JsonClass(generateAdapter = true)
data class LoginResponse(val user: UserDto, val token: String)

@JsonClass(generateAdapter = true)
data class PhoneLoginResponse(
    val user: UserDto,
    val customer: CustomerDto,
    val token: String
)

@JsonClass(generateAdapter = true)
data class MessageResponse(val message: String)

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int,
    val name: String,
    val role: String
)

@JsonClass(generateAdapter = true)
data class NameDto(val id: Int, val name: String)

@JsonClass(generateAdapter = true)
data class StatusDto(
    val id: Int,
    val name: String,
    @Json(name = "color_code") val colorCode: String? = null,
)

@JsonClass(generateAdapter = true)
data class CustomerDto(
    val id: Int,
    val name: String,
    val phone: String? = null,
    val email: String? = null,
    val address: String? = null,
    val notes: String? = null,
    val villas: List<VillaDto>? = null,
    @Json(name = "tower_units") val towerUnits: List<TowerUnitDto>? = null,
)

@JsonClass(generateAdapter = true)
data class VillaDto(
    val id: Int,
    val code: String,
    @Json(name = "is_sold") val isSold: Boolean = false,
    @Json(name = "customer_name") val customerName: String? = null,
    @Json(name = "completion_pct") val completionPct: Int? = null,
    @Json(name = "villa_type") val villaType: NameDto? = null,
    @Json(name = "current_stage") val currentStage: NameDto? = null,
    val status: StatusDto? = null,
    val engineer: NameDto? = null,
    @Json(name = "structural_status") val structuralStatus: StatusDto? = null,
    @Json(name = "finishing_status") val finishingStatus: StatusDto? = null,
    @Json(name = "facade_status") val facadeStatus: StatusDto? = null,
    @Json(name = "planned_start_date") val plannedStartDate: String? = null,
    @Json(name = "planned_finish_date") val plannedFinishDate: String? = null,
    @Json(name = "actual_start_date") val actualStartDate: String? = null,
    @Json(name = "actual_finish_date") val actualFinishDate: String? = null,
    @Json(name = "site_updates") val siteUpdates: List<SiteUpdateDto>? = null,
    val customer: CustomerDto? = null,
)

@JsonClass(generateAdapter = true)
data class TowerUnitDto(
    val id: Int,
    val code: String,
    @Json(name = "is_sold") val isSold: Boolean = false,
    @Json(name = "customer_name") val customerName: String? = null,
    @Json(name = "completion_pct") val completionPct: Int? = null,
    @Json(name = "tower_definition") val towerDefinition: NameDto? = null,
    @Json(name = "floor_definition") val floorDefinition: NameDto? = null,
    @Json(name = "current_stage") val currentStage: NameDto? = null,
    val status: StatusDto? = null,
    val engineer: NameDto? = null,
    @Json(name = "structural_status") val structuralStatus: StatusDto? = null,
    @Json(name = "finishing_status") val finishingStatus: StatusDto? = null,
    @Json(name = "facade_status") val facadeStatus: StatusDto? = null,
    val remarks: String? = null,
    @Json(name = "planned_start_date") val plannedStartDate: String? = null,
    @Json(name = "planned_finish_date") val plannedFinishDate: String? = null,
    @Json(name = "actual_start_date") val actualStartDate: String? = null,
    @Json(name = "actual_finish_date") val actualFinishDate: String? = null,
    @Json(name = "site_updates") val siteUpdates: List<SiteUpdateDto>? = null,
    val customer: CustomerDto? = null,
)

@JsonClass(generateAdapter = true)
data class SiteUpdateDto(
    val id: Int,
    val date: String? = null,
    val notes: String? = null,
    val photos: List<SitePhotoDto>? = null,
)

@JsonClass(generateAdapter = true)
data class SitePhotoDto(
    val id: Int,
    val url: String? = null,
)

@JsonClass(generateAdapter = true)
data class PaginationMeta(
    @Json(name = "current_page") val currentPage: Int,
    @Json(name = "last_page") val lastPage: Int,
    @Json(name = "per_page") val perPage: Int,
    val total: Int
)

@JsonClass(generateAdapter = true)
data class PaginatedResponse<T>(
    val data: List<T>,
    val meta: PaginationMeta? = null,
)

@JsonClass(generateAdapter = true)
data class VillaDetailResponse(val data: VillaDto)

@JsonClass(generateAdapter = true)
data class TowerUnitDetailResponse(val data: TowerUnitDto)

@JsonClass(generateAdapter = true)
data class CustomerDetailResponse(val data: CustomerDto)

@JsonClass(generateAdapter = true)
data class SetupListResponse<T>(val data: List<T>)

@JsonClass(generateAdapter = true)
data class StageDto(val id: Int, val name: String, val order: Int? = null)

// Reports

@JsonClass(generateAdapter = true)
data class DashboardStatsResponse(
    @Json(name = "total_villas") val totalVillas: Int = 0,
    @Json(name = "total_tower_units") val totalTowerUnits: Int = 0,
    @Json(name = "villas_sold") val villasSold: Int = 0,
    @Json(name = "tower_units_sold") val towerUnitsSold: Int = 0,
    @Json(name = "villas_sold_pct") val villasSoldPct: Double = 0.0,
    @Json(name = "tower_units_sold_pct") val towerUnitsSoldPct: Double = 0.0,
    @Json(name = "total_villa_tasks") val totalVillaTasks: Int = 0,
    @Json(name = "total_tower_tasks") val totalTowerTasks: Int = 0,
)

@JsonClass(generateAdapter = true)
data class SalesSummaryResponse(
    val villas: List<VillaSalesRow> = emptyList(),
    val towers: List<TowerSalesRow> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class VillaSalesRow(
    @Json(name = "villa_type_id") val villaTypeId: Int? = null,
    @Json(name = "villa_type_name") val villaTypeName: String? = null,
    @Json(name = "total_sold") val totalSold: Int = 0,
    @Json(name = "total_unsold") val totalUnsold: Int = 0,
    val total: Int = 0,
)

@JsonClass(generateAdapter = true)
data class TowerSalesRow(
    @Json(name = "tower_definition_id") val towerDefinitionId: Int? = null,
    @Json(name = "tower_name") val towerName: String? = null,
    @Json(name = "total_sold") val totalSold: Int = 0,
    @Json(name = "total_unsold") val totalUnsold: Int = 0,
    val total: Int = 0,
)

@JsonClass(generateAdapter = true)
data class StatusReportResponse(
    val villas: List<VillaStatusRow> = emptyList(),
    val towers: List<TowerStatusRow> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class VillaStatusRow(
    @Json(name = "status_name") val statusName: String? = null,
    @Json(name = "type_a_count") val typeACount: Int = 0,
    @Json(name = "type_b_count") val typeBCount: Int = 0,
    val total: Int = 0,
)

@JsonClass(generateAdapter = true)
data class TowerStatusRow(
    @Json(name = "status_name") val statusName: String? = null,
    @Json(name = "tower_1") val tower1: Int = 0,
    @Json(name = "tower_2") val tower2: Int = 0,
    @Json(name = "tower_3") val tower3: Int = 0,
    @Json(name = "tower_4") val tower4: Int = 0,
    @Json(name = "tower_5") val tower5: Int = 0,
    @Json(name = "tower_6") val tower6: Int = 0,
    val total: Int = 0,
)
