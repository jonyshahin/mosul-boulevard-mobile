package com.mbp.app.data.repository

import com.mbp.app.data.api.ApiService
import com.mbp.app.data.model.CustomerDto
import com.mbp.app.data.model.DashboardStatsResponse
import com.mbp.app.data.model.PaginatedResponse
import com.mbp.app.data.model.SalesSummaryResponse
import com.mbp.app.data.model.StatusReportResponse
import com.mbp.app.data.model.TowerUnitDto
import com.mbp.app.data.model.VillaDto
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StakeholderRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun getDashboardStats(): Result<DashboardStatsResponse> = wrap { api.dashboardStats() }

    suspend fun getVillas(
        page: Int,
        search: String?,
        villaTypeId: Int?,
    ): Result<PaginatedResponse<VillaDto>> = wrap { api.villas(page, search, villaTypeId) }

    suspend fun getVilla(id: Int): Result<VillaDto> = wrap { api.villa(id).data }

    suspend fun getTowerUnits(
        page: Int,
        search: String?,
        towerDefId: Int?,
    ): Result<PaginatedResponse<TowerUnitDto>> = wrap { api.towerUnits(page, search, towerDefId) }

    suspend fun getTowerUnit(id: Int): Result<TowerUnitDto> = wrap { api.towerUnit(id).data }

    suspend fun getCustomers(
        page: Int,
        search: String?,
    ): Result<PaginatedResponse<CustomerDto>> = wrap { api.customers(page, search) }

    suspend fun getCustomer(id: Int): Result<CustomerDto> = wrap { api.customer(id).data }

    suspend fun getSalesSummary(): Result<SalesSummaryResponse> = wrap { api.salesSummary() }
    suspend fun getStructuralStatus(): Result<StatusReportResponse> = wrap { api.structuralStatus() }
    suspend fun getFinishingStatus(): Result<StatusReportResponse> = wrap { api.finishingStatus() }
    suspend fun getFacadeStatus(): Result<StatusReportResponse> = wrap { api.facadeStatus() }

    private suspend inline fun <T> wrap(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: Exception) {
        val msg = if (e is HttpException) "Request failed (${e.code()})" else (e.message ?: "Unknown error")
        Result.failure(Exception(msg, e))
    }
}
