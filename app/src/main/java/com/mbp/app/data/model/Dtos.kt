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
    @Json(name = "color_code") val colorCode: String?
)

@JsonClass(generateAdapter = true)
data class CustomerDto(
    val id: Int,
    val name: String,
    val phone: String?,
    val email: String?,
    val address: String?,
    val notes: String?,
    val villas: List<VillaDto>?,
    @Json(name = "tower_units") val towerUnits: List<TowerUnitDto>?
)

@JsonClass(generateAdapter = true)
data class VillaDto(
    val id: Int,
    val code: String,
    @Json(name = "is_sold") val isSold: Boolean,
    @Json(name = "customer_name") val customerName: String?,
    @Json(name = "completion_pct") val completionPct: Int?,
    @Json(name = "villa_type") val villaType: NameDto?,
    @Json(name = "current_stage") val currentStage: NameDto?,
    val status: StatusDto?,
    val engineer: NameDto?,
    @Json(name = "structural_status") val structuralStatus: StatusDto?,
    @Json(name = "finishing_status") val finishingStatus: StatusDto?,
    @Json(name = "facade_status") val facadeStatus: StatusDto?,
    val customer: CustomerDto?
)

@JsonClass(generateAdapter = true)
data class TowerUnitDto(
    val id: Int,
    val code: String,
    @Json(name = "is_sold") val isSold: Boolean,
    @Json(name = "customer_name") val customerName: String?,
    @Json(name = "completion_pct") val completionPct: Int?,
    @Json(name = "tower_definition") val towerDefinition: NameDto?,
    @Json(name = "floor_definition") val floorDefinition: NameDto?,
    @Json(name = "current_stage") val currentStage: NameDto?,
    val status: StatusDto?,
    val engineer: NameDto?,
    @Json(name = "structural_status") val structuralStatus: StatusDto?,
    @Json(name = "finishing_status") val finishingStatus: StatusDto?,
    @Json(name = "facade_status") val facadeStatus: StatusDto?,
    val remarks: String?,
    val customer: CustomerDto?
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
    val meta: PaginationMeta?
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
data class StageDto(val id: Int, val name: String, val order: Int?)

// Reports

@JsonClass(generateAdapter = true)
data class DashboardStatsResponse(
    val villas: UnitStats,
    @Json(name = "tower_units") val towerUnits: UnitStats,
    val customers: CustomerStats?
)

@JsonClass(generateAdapter = true)
data class UnitStats(
    val total: Int,
    val sold: Int,
    val available: Int,
    @Json(name = "avg_completion") val avgCompletion: Double?
)

@JsonClass(generateAdapter = true)
data class CustomerStats(val total: Int)

@JsonClass(generateAdapter = true)
data class SalesSummaryResponse(
    val villas: SalesGroup,
    @Json(name = "tower_units") val towerUnits: SalesGroup
)

@JsonClass(generateAdapter = true)
data class SalesGroup(
    val total: Int,
    val sold: Int,
    val available: Int
)

@JsonClass(generateAdapter = true)
data class StatusReportResponse(
    val villas: List<StatusBucket>,
    @Json(name = "tower_units") val towerUnits: List<StatusBucket>
)

@JsonClass(generateAdapter = true)
data class StatusBucket(
    val status: StatusDto?,
    val count: Int
)
